package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bathtub
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.inmobixpress.inmobixpress.ui.components.ImageGallery
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.bathroomFormat
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.priceFormat

@Composable
fun DetailScreen(propertyItem: PropertyItem) {
    LazyColumn {
        item {
            ImageGallery(imageList = propertyItem.images)
            PropertyDetail(property = propertyItem)
            MapView(property = propertyItem)
        }
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
            fontSize = 11.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            OutlinedIconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "",
                )
            }
            Button(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1.0.toFloat()),
                onClick = {

                }) {
                Icon(
                    imageVector = Icons.Outlined.Whatsapp,
                    contentDescription = "",
                )
                Text(modifier = Modifier.padding(start = 4.dp), text = "WhatsApp")
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1.0.toFloat()),
                onClick = {

                }) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                )
                Text(modifier = Modifier.padding(start = 4.dp), text = "Contactar")
            }
        }
    }
}

@Composable
fun MapView(property: PropertyItem) {
    val latLng = LatLng(
        property.location.latitude,
        property.location.longitude
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 18f)
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = latLng),
            title = stringResource(id = property.type.typeId),
            snippet = property.district.name
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(previewListProperty()[0])
}