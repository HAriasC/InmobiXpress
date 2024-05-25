package com.inmobixpress.inmobixpress.main.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("Inmuebles", Icons.Outlined.Home, "Home")
    object Search : BottomNavItem("Buscar", Icons.Outlined.Search, "Search")
    object map : BottomNavItem("Mapa", Icons.Outlined.Map, "Search")
    object Profile : BottomNavItem("Cuenta", Icons.Outlined.Person, "Profile")
}