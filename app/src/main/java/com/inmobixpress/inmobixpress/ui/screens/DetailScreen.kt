package com.inmobixpress.inmobixpress.ui.screens

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bathtub
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.components.ImageGallery
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.bathroomFormat
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.priceFormat
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(viewModel: MainViewModel, property: PropertyItem) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 18f)
    }
    var columnScrollingEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            columnScrollingEnabled = true
        }
    }
    Column(
        Modifier.verticalScroll(
            state = rememberScrollState(),
            enabled = columnScrollingEnabled
        )
    ) {
        ImageGallery(imageList = property.images)
        PropertyDetail(property = property)
        MapView(
            property = property,
            cameraPositionState = cameraPositionState,
            onMapTouched = {
                columnScrollingEnabled = false
            }
        )
    }
}

@Composable
fun PropertyDetail(property: PropertyItem) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(text = "Alquiler", fontWeight = FontWeight.Medium)
        Text(text = "S/${property.price.toString().priceFormat()}", fontWeight = FontWeight.Medium)
        Text(
            text = "S/${property.maintenance} Mantenimiento",
            fontSize = 11.sp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.padding(vertical = 6.dp),
            text = "${property.address}, ${property.district.name}, ${property.district.province.name}",
            fontSize = 11.sp
        )
        Row {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .size(20.dp),
                imageVector = property.type.icon,
                contentDescription = "",
            )
            VerticalDivider(
                Modifier
                    .height(15.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .size(20.dp),
                imageVector = Icons.Outlined.SquareFoot,
                contentDescription = "",
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                text = "${property.area.toInt()} m²",
                fontSize = 10.sp
            )
            AnimatedVisibility(visible = property.bedrooms != 0) {
                Row {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .size(20.dp),
                        imageVector = Icons.Outlined.Bed,
                        contentDescription = "",
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        text = "${property.bedrooms} dorm.",
                        fontSize = 10.sp
                    )
                }
            }
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .size(20.dp),
                imageVector = Icons.Outlined.Bathtub,
                contentDescription = "",
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                text = "${property.bathrooms.toString().bathroomFormat()} baños",
                fontSize = 10.sp
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .size(20.dp),
                imageVector = Icons.Outlined.DirectionsCar,
                contentDescription = "",
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                text = "${property.garages} estac.",
                fontSize = 10.sp
            )
        }
        Text(
            text = property.description,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapView(
    property: PropertyItem,
    cameraPositionState: CameraPositionState,
    onMapTouched: () -> Unit,
) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }
    val scope = rememberCoroutineScope()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            onMapTouched()
                            false
                        }

                        else -> {
                            true
                        }
                    }
                },
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
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
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