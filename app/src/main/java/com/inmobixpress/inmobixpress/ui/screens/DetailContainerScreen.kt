package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.MainViewModel
import com.inmobixpress.inmobixpress.ui.components.SectionTabs
import com.inmobixpress.inmobixpress.ui.components.TopBar
import com.inmobixpress.inmobixpress.ui.utils.previewDetailTabList
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContainerScreen(
    viewModel: MainViewModel,
    id: Int,
    onNavigateBack: () -> Unit
) {
    val visible: Boolean by viewModel.contactBottomBarVisible.observeAsState(true)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Departamento en Alquiler",
                    visible = true,
                    enableBackAction = true,
                    onNavigateBack = onNavigateBack
                )
            },
            bottomBar = {
                AnimatedVisibility(visible = visible) {
                    ContactBottomBar()
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                SectionTabs(tabs = previewDetailTabList(previewListProperty()[id]))
            }
        }
    }
}

@Composable
fun ContactBottomBar() {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
        Row(modifier = Modifier.padding(8.dp)) {
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

@Preview
@Composable
fun DetailContainerScreenPreview() {
    DetailContainerScreen(MainViewModel(), id = 0) { }
}