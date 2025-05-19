package com.inmobixpress.inmobixpress.ui.screens

import android.opengl.GLSurfaceView
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.ar.GeoRenderer
import com.inmobixpress.inmobixpress.ui.ar.render.ARRender
import com.inmobixpress.inmobixpress.ui.components.MapTrackerBottomSheet
import com.inmobixpress.inmobixpress.ui.helpers.DisposableEffectArCoreWithLifeCycle
import com.inmobixpress.inmobixpress.ui.helpers.GeoPermissionsHelper
import com.inmobixpress.inmobixpress.ui.helpers.RequestGeoPermission
import com.inmobixpress.inmobixpress.ui.helpers.configureSession
import com.inmobixpress.inmobixpress.ui.model.AnchorItem
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

private typealias KeyedNearbyMarker = Pair<String, AnchorItem>

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveScreen(
    viewModel: MainViewModel,
    hostState: SnackbarHostState,
    onNavigateToDetail: (id: Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var permissionGranted by rememberSaveable { mutableStateOf(false) }
    var enableAR by rememberSaveable { mutableStateOf(false) }
    val sessionAR by viewModel.sessionAR.observeAsState()
    var geoRenderer: GeoRenderer? = null
    val showTrackerBottomSheet by viewModel.trackerBottomSheetVisible.observeAsState()
    val cameraPositionState = rememberCameraPositionState {
        val cameraTilt = if (position.zoom < 15) 0f else 60f
        position = CameraPosition.builder(
            CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 18f)
        ).tilt(cameraTilt).build()
    }
    val markerState = rememberMarkerState()
    var enableMap by rememberSaveable { mutableStateOf(false) }
    val rotationMarker = rememberSaveable { mutableFloatStateOf(0.0f) }

    BackPressHandler(onBackPressed = onNavigateBack)
    RequestGeoPermission(
        enableCamera = true,
        onPermissionGranted = {
            GeoPermissionsHelper.getLastUserLocation(
                context = context,
                onGetLastLocationSuccess = {
                    val locationText = "Location using LAST-LOCATION: LATITUDE: ${
                        it.first
                    }, LONGITUDE: ${it.second}"
                    Log.e("GPS", locationText)
                    scope.launch {
                        permissionGranted = true
                        hostState.currentSnackbarData?.performAction()
                    }
                },
                onGetLastLocationFailed = { exception ->

                },
                onGetLastLocationIsNull = {
                    GeoPermissionsHelper.getCurrentLocation(
                        context = context,
                        onGetCurrentLocationSuccess = {
                            val locationText = "Location using CURRENT-LOCATION: LATITUDE: ${
                                it.first
                            }, LONGITUDE: ${it.second}"
                            Log.e("GPS", locationText)
                            scope.launch {
                                permissionGranted = true
                                hostState.currentSnackbarData?.performAction()
                            }
                        },
                        onGetCurrentLocationFailed = {

                        }
                    )
                }
            )
        },
        onPermissionDenied = {
            scope.launch {
                permissionGranted = false
                hostState.showSnackbar(
                    message = "El permiso es requerido",
                    duration = SnackbarDuration.Indefinite
                )
            }
        },
        onPermissionsRevoked = {
            scope.launch {
                permissionGranted = false
                hostState.showSnackbar(
                    message = "El permiso es requerido",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    )
    DisposableEffectArCoreWithLifeCycle(
        permissionGranted = permissionGranted,
        onSessionCreated = { session ->
            viewModel.onSessionARChanged(session)
            configureSession(session)
            enableAR = true
            Log.e("ARCOREE", session.toString())
        },
        onExceptionLaunched = {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            enableAR = false
        },
        onResume = {
            Log.e("L", "onResume")
            viewModel.onVisibleChanged(false)
        },
        onDispose = {
            viewModel.onSessionARChanged(session = null)
            geoRenderer = null
            enableAR = false
        }
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.displayCutout),
        floatingActionButton = {
            if (enableMap) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onTrackerBottomSheetVisible(visible = true)
                    }
                ) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = "")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            TopAppBarDefaults.windowInsets.union(NavigationBarDefaults.windowInsets)
        )
    ) { innerPadding ->
        if (enableAR) {
            Log.e("ARCORE", sessionAR.toString())
            AndroidView(
                factory = { context ->
                    val surfaceView = GLSurfaceView(context)
                    geoRenderer = GeoRenderer(
                        viewModel = viewModel,
                        surfaceView = surfaceView,
                        session = sessionAR,
                        onSetMapPosition = {
                            Log.e("GEOP", it.toString())
                        },
                        onUpdateMapPosition = {
                            scope.launch {
                                enableMap = true
                                markerState.position = LatLng(it.latitude, it.longitude)
                                rotationMarker.floatValue = it.heading.toFloat()
                                val cameraTilt = if (
                                    cameraPositionState.position.zoom < 15
                                ) 0f else 60f
                                cameraPositionState.position = CameraPosition.builder(
                                    CameraPosition.fromLatLngZoom(
                                        LatLng(it.latitude, it.longitude),
                                        cameraPositionState.position.zoom
                                    )
                                ).bearing(
                                    cameraPositionState.position.bearing
                                ).tilt(cameraTilt).build()
                            }
                            viewModel.nearbyProperties.entries.forEach { it.value.anchor?.detach() }
                            viewModel.updateNearestProperties(
                                current = LatLng(it.latitude, it.longitude),
                                distance = 300.0
                            )
                            geoRenderer?.addAnchor(
                                latLng = LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                            Log.e("GEOAR", "Lat=${it.latitude} Lng=${it.longitude}")
                        },
                        onUpdateStatus = {
                            //Log.e("GEOS", it.toString())
                        },
                        onErrorMessage = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            Log.e("GEOE", it.toString())
                        }
                    )
                    ARRender(
                        surfaceView,
                        geoRenderer,
                        context.assets
                    )
                    surfaceView
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.displayCutout),
                update = {
                    it.onResume()
                },
                onReset = {
                    it.onPause()
                }
            )
            Text(text = "")
        }
        IconButton(onClick = {
            onNavigateBack()
        }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
        }
        if (showTrackerBottomSheet == true) {
            MapTrackerBottomSheet(
                viewModel = viewModel,
                cameraPositionState = cameraPositionState,
                markerState = markerState,
                rotationMarker = rotationMarker,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Composable
fun NearbyMarkers(
    keyedLocationMarker: Collection<KeyedNearbyMarker>,
    onNavigateToDetail: (id: Int) -> Unit,
) {
    keyedLocationMarker.forEach { (key, value) ->
        key(key) {
            val latLng = LatLng(
                value.property.location.latitude,
                value.property.location.longitude
            )
            MarkerComposable(
                state = MarkerState(position = latLng),
                anchor = Offset(0.45f, 0.5f),
                snippet = value.property.address,
                title = stringResource(id = value.property.type.typeId),
                onInfoWindowClick = {
                    onNavigateToDetail(value.property.id)
                }
            ) {
                FilledIconButton(onClick = { }) {
                    Icon(imageVector = value.property.type.icon, contentDescription = "")
                }
            }
        }
    }
}

@Preview
@Composable
fun LiveScreenPreview() {
    LiveScreen(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        hostState = rememberSaveable { SnackbarHostState() },
        onNavigateToDetail = { },
        onNavigateBack = { }
    )
}