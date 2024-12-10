package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
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
import com.inmobixpress.inmobixpress.ui.model.FilterType
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.model.ServiceMarker
import com.inmobixpress.inmobixpress.ui.theme.PurpleGrey40
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.search
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

private typealias KeyedLocationData = Pair<String, ServiceMarker>

@Composable
fun ServiceScreen(viewModel: MainViewModel, property: PropertyItem) {
    val context = LocalContext.current
    // Initialize the SDK
    Places.initializeWithNewPlacesApiEnabled(context, "AIzaSyCgQtOFMwKZXP7ABlthN7OR19hvqFFlKt4")
    // Create a new PlacesClient instance
    val placesClient = Places.createClient(context)
    Box(modifier = Modifier.fillMaxSize()) {
        ServiceMap(viewModel = viewModel, property = property)
        Filter(viewModel = viewModel, property = property, placesClient = placesClient)
    }
}

@Composable
fun ServiceMap(viewModel: MainViewModel, property: PropertyItem) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 16f)
    }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }
    val scaleBackground = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)
    val scaleBorderStroke = BorderStroke(width = 1.dp, DarkGray.copy(alpha = 0.2f))
    val scope = rememberCoroutineScope()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = properties
            ) {
                MarkerComposable(
                    state = MarkerState(position = latLng),
                    anchor = Offset(0.45f, 0.5f),
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(
                                        latLng,
                                        18f
                                    )
                                )
                            )
                        }
                        true
                    }) {
                    FilledIconButton(onClick = { }) {
                        Icon(imageVector = property.type.icon, contentDescription = "")
                    }
                }
                ServiceMarkers(keyedLocationData = viewModel.serviceMarkers.toList())
            }
            Box(
                modifier = Modifier
                    .padding(top = 54.dp, end = 5.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        scaleBackground,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .border(
                        scaleBorderStroke,
                        shape = MaterialTheme.shapes.medium
                    ),
            ) {
                ScaleBar(
                    modifier = Modifier.padding(end = 4.dp),
                    height = 70.dp,
                    cameraPositionState = cameraPositionState
                )
            }
        }
    }
}

@Composable
fun Filter(viewModel: MainViewModel, property: PropertyItem, placesClient: PlacesClient) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp)
        ) {
            viewModel.filterList().forEach { type ->
                ItemFilter(
                    filterType = type,
                    onFilterClick = { types, filterType ->
                        placesClient.search(includedTypes = types, latLng = latLng) { response ->
                            response.places.forEach { place ->
                                viewModel.serviceMarkers += place.id to ServiceMarker(
                                    place = place,
                                    type = filterType
                                )
                            }
                        }
                    },
                    onRemoveAllMarkers = { label ->
                        viewModel.serviceMarkers.entries.removeIf { it.value.type.label == label }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemFilter(
    filterType: FilterType,
    onFilterClick: (List<String>, FilterType) -> Unit,
    onRemoveAllMarkers: (String) -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(false) }
    FilterChip(
        onClick = {
            if (!selected) {
                onFilterClick(filterType.includedTypes, filterType)
            } else {
                onRemoveAllMarkers(filterType.label)
            }
            selected = !selected
        },
        label = {
            Text(text = filterType.label)
        },
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = selected,
        leadingIcon = {
            Icon(
                imageVector = filterType.icon,
                contentDescription = "",
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    )
}

@Composable
fun ServiceMarkers(keyedLocationData: Collection<KeyedLocationData>) {
    keyedLocationData.forEach { (key, marker) ->
        key(key) {
            MarkerComposable(
                state = MarkerState(position = marker.place.latLng),
                anchor = Offset(0.45f, 0.5f),
                title = marker.place.name
            ) {
                OutlinedIconButton(
                    onClick = { },
                    colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = PurpleGrey40)
                ) {
                    Icon(
                        imageVector = marker.type.icon,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ServiceScreenPreview() {
    ServiceScreen(
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