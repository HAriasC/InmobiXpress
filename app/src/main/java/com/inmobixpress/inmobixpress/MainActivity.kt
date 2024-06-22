package com.inmobixpress.inmobixpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.inmobixpress.inmobixpress.ui.screens.MainScreen
import com.inmobixpress.inmobixpress.ui.theme.InmobiXpressAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            InmobiXpressAppTheme {
                MainScreen()
            }
        }
    }
}