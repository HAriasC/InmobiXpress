/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inmobixpress.inmobixpress.ui.ar

import android.annotation.SuppressLint
import android.app.Activity
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.GuardedBy
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Anchor
import com.google.ar.core.Earth
import com.google.ar.core.Frame
import com.google.ar.core.GeospatialPose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.ar.helpers.DisplayRotationHelper
import com.inmobixpress.inmobixpress.ui.ar.helpers.TrackingStateHelper
import com.inmobixpress.inmobixpress.ui.ar.render.ARRender
import com.inmobixpress.inmobixpress.ui.ar.render.Framebuffer
import com.inmobixpress.inmobixpress.ui.ar.render.Mesh
import com.inmobixpress.inmobixpress.ui.ar.render.Shader
import com.inmobixpress.inmobixpress.ui.ar.render.Texture
import com.inmobixpress.inmobixpress.ui.ar.render.Triangle
import com.inmobixpress.inmobixpress.ui.ar.render.arcore.BackgroundRenderer
import com.inmobixpress.inmobixpress.ui.model.AnchorItem
import java.io.IOException

class GeoRenderer(
    viewModel: MainViewModel,
    surfaceView: GLSurfaceView,
    session: Session?,
    onSetMapPosition: (LatLng) -> Unit,
    onUpdateMapPosition: (GeospatialPose) -> Unit,
    onUpdateStatus: (Earth) -> Unit,
    onErrorMessage: (String) -> Unit
) : ARRender.Renderer, DefaultLifecycleObserver {
    //<editor-fold desc="ARCore initialization" defaultstate="collapsed">
    companion object {
        val TAG = "GeoRenderer"

        private val Z_NEAR = 0.1f
        private val Z_FAR = 1000f
    }

    lateinit var backgroundRenderer: BackgroundRenderer
    lateinit var virtualSceneFramebuffer: Framebuffer
    var hasSetTextureNames = false

    // Virtual object (ARCore pawn)
    lateinit var virtualObjectMesh: Mesh
    lateinit var virtualObjectShader: Shader
    lateinit var virtualObjectTexture: Texture

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    val modelMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)
    val projectionMatrix = FloatArray(16)
    val modelViewMatrix = FloatArray(16) // view x model

    val modelViewProjectionMatrix = FloatArray(16) // projection x view x model

    val sessionAR = session
    val updateMapPosition = onUpdateMapPosition
    val updateStatus = onUpdateStatus
    val setMapPosition = onSetMapPosition
    val errorMessage = onErrorMessage

    val displayRotationHelper = DisplayRotationHelper(surfaceView.context)
    val trackingStateHelper = TrackingStateHelper(surfaceView.context as Activity)

    val context = surfaceView.context
    private val glSurfaceView = surfaceView

    private val anchorLock = Any()

    // Locks needed for synchronization
    private val singleTapLock = Any()

    @GuardedBy("singleTapLock")
    private var queuedSingleTap: MotionEvent? = null

    // Tap handling and UI.
    private lateinit var gestureDetector: GestureDetector

    private val mainViewModel = viewModel

    override fun onResume(owner: LifecycleOwner) {
        displayRotationHelper.onResume()
        sessionAR?.resume()
        hasSetTextureNames = false
    }

    override fun onPause(owner: LifecycleOwner) {
        sessionAR?.pause()
        displayRotationHelper.onPause()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onSurfaceCreated(render: ARRender) {
        // Prepare the rendering objects.
        // This involves reading shaders and 3D model files, so may throw an IOException.
        try {
            backgroundRenderer = BackgroundRenderer(render)
            virtualSceneFramebuffer = Framebuffer(render, /*width=*/ 1, /*height=*/ 1)

            // Virtual object to render (Geospatial Marker)
            virtualObjectTexture =
                Texture.createFromAsset(
                    render,
                    "models/texture_red.jpg",
                    Texture.WrapMode.CLAMP_TO_EDGE,
                    Texture.ColorFormat.SRGB
                )

            virtualObjectMesh = Mesh.createFromAsset(render, "models/map_pointer.obj")
            virtualObjectShader =
                Shader.createFromAssets(
                    render,
                    "shaders/ar_unlit_object.vert",
                    "shaders/ar_unlit_object.frag",
                    /*defines=*/ null
                )
                    .setTexture("u_Texture", virtualObjectTexture)

            backgroundRenderer.setUseDepthVisualization(render, false)
            backgroundRenderer.setUseOcclusion(render, false)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read a required asset file", e)
            showError("Failed to read a required asset file: $e")
        }
        (context as Activity).runOnUiThread {
            // Set up touch listener.
            gestureDetector =
                GestureDetector(
                    context,
                    object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapUp(e: MotionEvent): Boolean {
                            synchronized(singleTapLock) {
                                queuedSingleTap = e
                            }
                            return true
                        }

                        override fun onDown(e: MotionEvent): Boolean {
                            return true
                        }
                    })
            glSurfaceView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(
                    event
                )
            }
            Log.e("ARTRACK", "INIT")
        }
        Log.e("GEOAR", "onSC")
    }

    override fun onSurfaceChanged(render: ARRender, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
        virtualSceneFramebuffer.resize(width, height)
    }
    //</editor-fold>

    override fun onDrawFrame(render: ARRender) {
        val session = sessionAR ?: return

        //<editor-fold desc="ARCore frame boilerplate" defaultstate="collapsed">
        // Texture names should only be set once on a GL thread unless they change. This is done during
        // onDrawFrame rather than onSurfaceCreated since the session is not guaranteed to have been
        // initialized during the execution of onSurfaceCreated.
        if (!hasSetTextureNames) {
            session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))
            hasSetTextureNames = true
        }

        // -- Update per-frame state

        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper.updateSessionIfNeeded(session)

        // Obtain the current frame from ARSession. When the configuration is set to
        // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
        // camera framerate.
        val frame = try {
            session.update()
        } catch (e: CameraNotAvailableException) {
            Log.e(TAG, "Camera not available during onDrawFrame", e)
            showError("Camera not available. Try restarting the app.")
            return
        }

        val camera = frame.camera

        // BackgroundRenderer.updateDisplayGeometry must be called every frame to update the coordinates
        // used to draw the background camera image.
        backgroundRenderer.updateDisplayGeometry(frame)

        // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
        trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

        // -- Draw background
        if (frame.timestamp != 0L) {
            // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
            // drawing possible leftover data from previous sessions if the texture is reused.
            backgroundRenderer.drawBackground(render)
        }

        // If not tracking, don't draw 3D objects.
        if (camera.trackingState == TrackingState.PAUSED) {
            return
        }

        // Get projection matrix.
        camera.getProjectionMatrix(projectionMatrix, 0, Z_NEAR, Z_FAR)

        // Get camera matrix and draw.
        camera.getViewMatrix(viewMatrix, 0)

        render.clear(virtualSceneFramebuffer, 0f, 0f, 0f, 0f)
        //</editor-fold>

        val earth = session.earth
        if (earth?.trackingState == TrackingState.TRACKING) {
            val cameraGeospatialPose = earth.cameraGeospatialPose
            this.updateMapPosition(cameraGeospatialPose)
            this.updateStatus(earth)
        }

        // Draw the placed anchors, if it exists.
        updateAnchors(frame = frame, render = render)

        // Compose the virtual scene with the background.
        backgroundRenderer.drawVirtualScene(render, virtualSceneFramebuffer, Z_NEAR, Z_FAR)

        synchronized(singleTapLock) {
            if (queuedSingleTap != null) {
                mainViewModel.nearbyProperties.forEach { property ->
                    rayPicking(
                        item = property.value,
                        viewWidth = virtualSceneFramebuffer.width,
                        viewHeight = virtualSceneFramebuffer.height,
                        rx = queuedSingleTap!!.x,
                        ry = queuedSingleTap!!.y
                    )
                }
                queuedSingleTap = null
            }
        }
    }

    var earthAnchor: Anchor? = null

    fun addAnchor(latLng: LatLng) {
        val earth = sessionAR?.earth ?: return
        if (earth.trackingState != TrackingState.TRACKING) {
            return
        }
        earthAnchor?.detach()

        // Place the earth anchor at the same altitude as that of the camera to make it easier to view.
        val cameraGeospatialPose = earth.cameraGeospatialPose
        val altitude = cameraGeospatialPose.altitude - 1
        // The rotation quaternion of the anchor in EUS coordinates.
        val qx = 0f
        val qy = 0f
        val qz = 0f
        val qw = 1f
        earthAnchor = earth.createAnchor(
            latLng.latitude, latLng.longitude, altitude, qx, qy, qz, qw
        )

        setMapPosition(latLng)
    }

    fun updateAnchors(frame: Frame, render: ARRender) {
        val earth = sessionAR?.earth ?: return
        if (earth.trackingState != TrackingState.TRACKING) {
            return
        }
        synchronized(anchorLock) {
            mainViewModel.nearbyProperties.forEach { property ->

                val cameraGeospatialPose = earth.cameraGeospatialPose
                val altitude = cameraGeospatialPose.altitude - 1 - 5
                Log.e("GEOAr", altitude.toString())
                // The rotation quaternion of the anchor in EUS coordinates.
                val qx = 0f
                val qy = 0f
                val qz = 0f
                val qw = 1f
                property.value.anchor = earth.createAnchor(
                    property.value.property.location.latitude,
                    property.value.property.location.longitude,
                    altitude,
                    qx,
                    qy,
                    qz,
                    qw
                )
                property.value.anchor?.let { anchor ->
                    render.renderCompassAtAnchor(
                        item = property.value,
                        frame = frame,
                        anchor = anchor
                    )

                }
            }
            Log.e("GEOAR", mainViewModel.nearbyProperties.map { it.value.anchor }.toString())
        }
    }

    private fun ARRender.renderCompassAtAnchor(
        item: AnchorItem, frame: Frame, anchor: Anchor
    ) {
        // Get the current pose of the Anchor in world space. The Anchor pose is updated
        // during calls to session.update() as ARCore refines its estimate of the world.
        anchor.pose.toMatrix(modelMatrix, 0)

        // Calculate model/view/projection matrices
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.090f * time.toInt()
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            modelViewMatrix,
            0
        )
        Matrix.rotateM(modelViewProjectionMatrix, 0, angle, 0f, -1.0f, 0f)
        // Update shader properties and draw
        item.viewMatrix = viewMatrix.clone()
        item.modelViewMatrix = modelViewMatrix.clone()
        item.projectionMatrix = modelViewProjectionMatrix.clone()
        item.mesh = virtualObjectMesh
        virtualObjectShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
        draw(virtualObjectMesh, virtualObjectShader, virtualSceneFramebuffer)
    }

    fun rayPicking(
        item: AnchorItem,
        viewWidth: Int,
        viewHeight: Int,
        rx: Float,
        ry: Float
    ) {
        Log.e(
            "Ray ${item.property.id}",
            "${item.viewMatrix?.map { it }} ${item.modelViewMatrix?.map { it }} ${item.projectionMatrix?.map { it }}"
        )
        Log.e("Ray", "${queuedSingleTap?.x} ${queuedSingleTap?.y}")
        val near_xyz: FloatArray = unProject(
            rx, ry, 0f, item.viewMatrix!!, item.projectionMatrix!!, viewWidth, viewHeight
        )
        val far_xyz: FloatArray = unProject(
            rx, ry, 1f, item.viewMatrix!!, item.projectionMatrix!!, viewWidth, viewHeight
        )
        Log.e("RayZ", "${item.viewMatrix!!.map { it }} ${item.projectionMatrix!!.map { it }} $rx $ry")
        Log.e("RayNF", "${near_xyz.map { it }} ${far_xyz.map { it }}")

        Log.e("RayV",item.mesh?.vertices?.map { it }.toString())

        val coordCount: Int = item.mesh!!.vertices.size
        val convertedSquare = FloatArray(coordCount)
        val resultVector = FloatArray(4)
        val inputVector = FloatArray(4)

        run {
            var i = 0
            while (i < coordCount) {
                inputVector[0] = item.mesh!!.vertices[i]
                inputVector[1] = item.mesh!!.vertices[i + 1]
                inputVector[2] = item.mesh!!.vertices[i + 2]
                inputVector[3] = 1f
                Matrix.multiplyMV(
                    resultVector,
                    0,
                    item.modelViewMatrix,
                    0,
                    inputVector,
                    0
                )
                convertedSquare[i] = resultVector[0] / resultVector[3]
                convertedSquare[i + 1] = resultVector[1] / resultVector[3]
                convertedSquare[i + 2] = resultVector[2] / resultVector[3]
                i += 3
            }
        }
        val triangleList: MutableList<Triangle> = ArrayList<Triangle>()
        var i = 0
        while (i < convertedSquare.size / 9) {
            val triangle = Triangle(
                floatArrayOf(
                    convertedSquare[9 * i],
                    convertedSquare[9 * i + 1], convertedSquare[9 * i + 2]
                ), floatArrayOf(
                    convertedSquare[9 * i + 3],
                    convertedSquare[9 * i + 4], convertedSquare[9 * i + 5]
                ), floatArrayOf(
                    convertedSquare[9 * i + 6],
                    convertedSquare[9 * i + 7], convertedSquare[9 * i + 8]
                )
            )
            triangleList.add(triangle)
            i += 1
        }

        var intersects = 0
        for (triangle in triangleList) {
            val point1 = FloatArray(3)
            intersects = Triangle.intersectRayAndTriangle(near_xyz, far_xyz, triangle, point1)
            if (intersects == 1 || intersects == 2) {
                Log.e("Ray", "touch!: ${item.property.id}")
                return
            }
        }
    }

    private fun unProject(
        xTouch: Float, yTouch: Float, winz: Float,
        viewMatrix: FloatArray,
        projMatrix: FloatArray,
        width: Int, height: Int
    ): FloatArray {
        val viewport = intArrayOf(0, 0, width, height)

        val out = FloatArray(3)
        val temp = FloatArray(4)
        val temp2 = FloatArray(4)

        // get the near and far ords for the click
        val winx = xTouch
        val winy = viewport[3].toFloat() - yTouch

        val result = GLU.gluUnProject(
            winx,
            winy,
            winz,
            viewMatrix,
            0,
            projMatrix,
            0,
            viewport,
            0,
            temp,
            0
        )

        Matrix.multiplyMV(temp2, 0, viewMatrix, 0, temp, 0)
        if (result == 1) {
            out[0] = temp2[0] / temp2[3]
            out[1] = temp2[1] / temp2[3]
            out[2] = temp2[2] / temp2[3]
        }
        return out
    }

    private fun showError(errorMessage: String) =
        this.errorMessage(errorMessage)

}
