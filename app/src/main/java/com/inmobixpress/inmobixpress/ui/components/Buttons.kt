package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchFloatingActionButton(onNavigateToSearch: () -> Unit) {
    FloatingActionButton(
        onClick = onNavigateToSearch,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
    }
}

@Preview
@Composable
fun FloatingActionButtonDockedPreview() {
    SearchFloatingActionButton() { }
}