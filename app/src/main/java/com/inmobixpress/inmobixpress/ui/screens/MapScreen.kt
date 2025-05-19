package com.inmobixpress.inmobixpress.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.DarkGray
import com.google.maps.android.compose.widgets.ScaleBar
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.helpers.GeoPermissionsHelper
import com.inmobixpress.inmobixpress.ui.helpers.RequestGeoPermission
import com.inmobixpress.inmobixpress.ui.model.District
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.getCity
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

private typealias KeyedLocationMarker = Pair<String, PropertyItem>

@Composable
fun MapScreen(
    viewModel: MainViewModel,
    hostState: SnackbarHostState,
    onNavigateToDetail: (id: Int) -> Unit,
    onNavigateToSearch: (id: Int) -> Unit
) {
    val context = LocalContext.current
    // State variables to manage location information and permission result text
    var locationText by rememberSaveable { mutableStateOf("No location obtained :(") }
    var permissionResultText by rememberSaveable { mutableStateOf("Permission Granted...") }

    val miraflores = LatLng(-12.122512, -77.031388)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(miraflores, 14f)
    }
    val scope = rememberCoroutineScope()

    // Request location permission using a Compose function
    RequestGeoPermission(
        enableCamera = false,
        onPermissionGranted = {
            GeoPermissionsHelper.getLastUserLocation(
                context = context,
                onGetLastLocationSuccess = {
                    locationText = "Location using LAST-LOCATION: LATITUDE: ${
                        it.first
                    }, LONGITUDE: ${it.second}"
                    Log.e("GPS", locationText)
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(it.first, it.second),
                                    14f
                                )
                            )
                        )
                        Log.e("GPSL", context.getCity(LatLng(it.first, it.second)))
                        if (viewModel.foundProperties.isEmpty()) {
                            val district = context.getCity(LatLng(it.first, it.second))
                            viewModel.updateFoundProperties(district)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(
                                        LatLng(it.first, it.second),
                                        14f
                                    )
                                )
                            )
                        }
                        hostState.currentSnackbarData?.performAction()
                    }
                },
                onGetLastLocationFailed = { exception ->
                    locationText = exception.localizedMessage ?: "Error Getting Last Location"
                    Log.e("GPS", locationText)
                },
                onGetLastLocationIsNull = {
                    // Attempt to get the current user location
                    GeoPermissionsHelper.getCurrentLocation(
                        context = context,
                        onGetCurrentLocationSuccess = {
                            scope.launch {
                                locationText = "Location using CURRENT-LOCATION: LATITUDE: ${
                                    it.first
                                }, LONGITUDE: ${it.second}"
                                Log.e("GPS", locationText)
                                Log.e("GPSC", context.getCity(LatLng(it.first, it.second)))
                                if (viewModel.foundProperties.isEmpty()) {
                                    val district = context.getCity(LatLng(it.first, it.second))
                                    viewModel.updateFoundProperties(district)
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newCameraPosition(
                                            CameraPosition.fromLatLngZoom(
                                                LatLng(it.first, it.second),
                                                14f
                                            )
                                        )
                                    )
                                }
                                hostState.currentSnackbarData?.performAction()
                            }
                        },
                        onGetCurrentLocationFailed = {
                            locationText =
                                it.localizedMessage
                                    ?: "Error Getting Current Location"
                            Log.e("GPS", locationText)
                        }
                    )
                }
            )
        },
        onPermissionDenied = {
            permissionResultText = "Permission Denied :("
            Log.e("GPS", permissionResultText)
            scope.launch {
                hostState.showSnackbar(
                    message = "El permiso es requerido",
                    duration = SnackbarDuration.Indefinite
                )
            }
        },
        onPermissionsRevoked = {
            permissionResultText = "Permission Revoked :("
            Log.e("GPS", permissionResultText)
            scope.launch {
                hostState.showSnackbar(
                    message = "El permiso es requerido",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    )
    Box(
        Modifier.fillMaxSize()
    ) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val properties by remember {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.TERRAIN
                )
            )
        }
        val scaleBackground = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)
        val scaleBorderStroke = BorderStroke(width = 1.dp, DarkGray.copy(alpha = 0.2f))
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            onMapLoaded = {
                isMapLoaded = true
            }
        ) {
            FoundMarkers(
                keyedLocationMarker = viewModel.foundProperties.toList(),
                onNavigateToDetail = onNavigateToDetail
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 88.dp, end = 7.dp)
                .align(alignment = Alignment.TopEnd)
                .background(
                    color = scaleBackground,
                    shape = MaterialTheme.shapes.medium,
                )
                .border(
                    border = scaleBorderStroke,
                    shape = MaterialTheme.shapes.medium
                ),
        ) {
            ScaleBar(
                modifier = Modifier.padding(end = 4.dp),
                height = 70.dp,
                cameraPositionState = cameraPositionState
            )
        }
        SearchPropertyBar(
            viewModel = viewModel,
            id = 1,
            onNavigateToSearch = onNavigateToSearch
        ) { district ->
            val location = LatLng(
                district.location.latitude,
                district.location.longitude
            )
            viewModel.updateFoundProperties(district.name)
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            location,
                            14f
                        )
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPropertyBar(
    viewModel: MainViewModel,
    id: Int,
    onNavigateToSearch: (id: Int) -> Unit,
    onRefresh: () -> Unit = {},
    onItemClick: (District) -> Unit
) {
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .semantics { isTraversalGroup = true }) {
        DockedSearchBar(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = viewModel.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Ingresa un distrito") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") },
                    trailingIcon = {
                        if (expanded) {
                            IconButton(
                                onClick = {
                                    if (viewModel.searchQuery.isNotEmpty()) {
                                        viewModel.onSearchQueryChange("")
                                        viewModel.foundProperties.entries.clear()
                                        onRefresh()
                                    } else {
                                        expanded = false
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = ""
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    onNavigateToSearch(id)
                                    expanded = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            if (searchResults.isEmpty()) {
                PropertyListEmptyState()
            } else {
                LazyColumn(
                ) {
                    items(
                        count = searchResults.size,
                        key = { index -> searchResults[index].id },
                    ) { index ->
                        ListItem(
                            headlineContent = { Text(text = searchResults[index].name) },
                            supportingContent = { Text(text = searchResults[index].province.name) },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.LocationOn,
                                    contentDescription = null
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            modifier =
                            Modifier
                                .clickable {
                                    val district = searchResults[index]
                                    onItemClick(district)
                                    expanded = false
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyListEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "No se encontraron distritos que coincidan con el texto",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Pruebe con otro distrito",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FoundMarkers(
    keyedLocationMarker: Collection<KeyedLocationMarker>,
    onNavigateToDetail: (id: Int) -> Unit
) {
    keyedLocationMarker.forEach { (key, property) ->
        key(key) {
            val latLng = LatLng(
                property.location.latitude,
                property.location.longitude
            )
            MarkerComposable(
                state = MarkerState(position = latLng),
                anchor = Offset(0.45f, 0.5f),
                snippet = property.address,
                title = stringResource(id = property.type.typeId),
                onInfoWindowClick = {
                    onNavigateToDetail(property.id)
                }
            ) {
                FilledIconButton(onClick = { }) {
                    Icon(imageVector = property.type.icon, contentDescription = "")
                }
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        hostState = rememberSaveable { SnackbarHostState() },
        onNavigateToDetail = { },
        onNavigateToSearch = { }
    )
}