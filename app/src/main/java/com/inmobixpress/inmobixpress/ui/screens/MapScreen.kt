package com.inmobixpress.inmobixpress.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import kotlinx.coroutines.launch

@Composable
fun MapScreen(onNavigateToDetail: (id: Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val miraflores = LatLng(-12.121498, -77.028652)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(miraflores, 14f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            previewListProperty().forEach { property ->
                val latLng = LatLng(
                    property.location.latitude,
                    property.location.longitude
                )
                Marker(
                    state = MarkerState(position = latLng),
                    title = stringResource(id = property.type.typeId),
                    snippet = property.district.name,
                    onInfoWindowClick = {
                        Log.e("PROP", property.address)
                        onNavigateToDetail(property.id)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen {

    }
}