package com.inmobixpress.inmobixpress.main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inmobixpress.inmobixpress.main.ui.model.BottomNavItem

@Composable
fun Container() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar() }
    ) { innerPadding ->

    }
}

@Composable
fun BottomBar() {
    NavigationBar {
    }
}
