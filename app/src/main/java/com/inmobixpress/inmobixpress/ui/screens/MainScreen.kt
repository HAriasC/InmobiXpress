package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.inmobixpress.inmobixpress.ui.components.BottomBar
import com.inmobixpress.inmobixpress.ui.navigation.MainNavigation

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = { BottomBar(navController = navController, true) },

    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            MainNavigation(navController = navController)
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
