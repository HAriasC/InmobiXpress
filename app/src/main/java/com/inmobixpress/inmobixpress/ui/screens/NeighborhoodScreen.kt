package com.inmobixpress.inmobixpress.ui.screens

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewPanoramaCamera
import com.google.maps.android.Status
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.compose.streetview.StreetViewCameraPositionState
import com.google.maps.android.compose.streetview.rememberStreetViewCameraPositionState
import com.google.maps.android.ktx.MapsExperimentalFeature
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.theme.PurpleGrey40
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@OptIn(MapsExperimentalFeature::class)
@Composable
fun NeighborhoodScreen(viewModel: MainViewModel, property: PropertyItem) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    var streetViewResult by remember { mutableStateOf(Status.NOT_FOUND) }
    val camera = rememberStreetViewCameraPositionState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 18f)
    }
    val markerState = rememberMarkerState(position = latLng)
    val dragged = remember { mutableStateOf(false) }
    val rotationMarker = remember { mutableFloatStateOf(0.0f) }
    LaunchedEffect(cameraPositionState, camera, markerState) {
        launch {
            snapshotFlow { camera.panoramaCamera }
                .collect {
                    Log.e("SV", "Camera at: $it")
                    rotationMarker.floatValue = it.bearing
                }
        }
        launch {
            snapshotFlow { camera.location }
                .collect {
                    launch {
                        Log.e("SW", "Location at: $it")
                        markerState.position = it.position
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    it.position,
                                    18f
                                )
                            )
                        )
                    }
                }
        }
        launch {
            snapshotFlow { markerState.dragState to markerState.position }
                .collect { (dragState, position) ->
                    launch {
                        Log.e("SWM", "$dragState $position")
                        if (dragState == DragState.DRAG) {
                            dragged.value = true
                        }
                        if (dragState == DragState.END && dragged.value
                            && position.latitude != 0.0 && position.longitude != 0.0
                        ) {
                            launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.fromLatLngZoom(
                                            position,
                                            18f
                                        )
                                    )
                                )
                                camera.setPosition(position)
                                camera.animateTo(
                                    StreetViewPanoramaCamera(
                                        camera.panoramaCamera.tilt,
                                        camera.panoramaCamera.tilt,
                                        camera.panoramaCamera.bearing
                                    ), 1000
                                )
                                dragged.value = false
                            }
                        }
                        if (position.latitude == 0.0 && position.longitude == 0.0) {
                            Log.e("Track0", position.toString())
                            viewModel.onTrackerMapChanged(true)
                        }
                        if (Math.round(position.latitude) == Math.round(latLng.latitude) && Math.round(
                                position.longitude
                            ) == Math.round(latLng.longitude)
                        ) {
                            Log.e("TRACK", position.toString())
                            viewModel.onTrackerMapChanged(false)
                        }
                    }
                }
        }
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            StreetView(
                Modifier.matchParentSize(),
                cameraPositionState = camera,
                streetViewPanoramaOptionsFactory = {
                    StreetViewPanoramaOptions().position(
                        LatLng(
                            property.location.latitude,
                            property.location.longitude
                        )
                    )
                },
                isPanningGesturesEnabled = true,
                isZoomGesturesEnabled = true,
                onClick = {
                    Log.d("SV", "Street view clicked")
                },
                onLongClick = {
                    Log.d("SV", "Street view long clicked")
                }
            )
            TrackerMap(
                viewModel = viewModel,
                property = property,
                cameraPositionState = cameraPositionState,
                camera = camera,
                markerState = markerState,
                rotationMarker = rotationMarker
            )
        }
    }
}

@Composable
fun TrackerMap(
    viewModel: MainViewModel,
    property: PropertyItem,
    cameraPositionState: CameraPositionState,
    camera: StreetViewCameraPositionState,
    markerState: MarkerState,
    rotationMarker: MutableFloatState,
) {
    Log.e("GPS", "${rotationMarker.floatValue}")
    val showLoader by viewModel.trackerMapVisible.observeAsState()
    ElevatedCard(
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        val latLng = LatLng(
            property.location.latitude,
            property.location.longitude
        )
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                MarkerComposable(
                    state = MarkerState(position = latLng),
                    anchor = Offset(0.5f, 0.5f),
                    onClick = {
                        camera.setPosition(latLng)
                        camera.animateTo(
                            camera = StreetViewPanoramaCamera(
                                camera.panoramaCamera.zoom,
                                camera.panoramaCamera.tilt,
                                camera.panoramaCamera.bearing
                            ), durationMs = 1000
                        )
                        true
                    }
                ) {
                    FilledIconButton(onClick = { }) {
                        Icon(imageVector = property.type.icon, contentDescription = "")
                    }
                }
                MarkerComposable(
                    state = markerState,
                    anchor = Offset(0.5f, 0.5f),
                    draggable = true,
                    rotation = rotationMarker.floatValue
                ) {
                    OutlinedIconButton(
                        onClick = { },
                        colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = PurpleGrey40),
                        border = BorderStroke(
                            width = 1.dp,
                            color = PurpleGrey40
                        )
                    ) {
                        Icon(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.ic_navigation_white_48dp),
                            contentDescription = "",
                            modifier = Modifier.scale(0.5f, 0.5f),
                            tint = PurpleGrey40
                        )
                    }
                }
            }
            if (showLoader == true) {
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier
                        .matchParentSize(),
                    visible = showLoader!!,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NeighborhoodScreenPreview() {
    NeighborhoodScreen(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        property = previewListProperty()[0]
    )
}