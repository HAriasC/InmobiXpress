package com.inmobixpress.inmobixpress.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraOutdoor
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MapsHomeWork
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.ui.navigation.NavScreen

enum class BottomNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
    val route: String
) {
    HOME(
        selectedIcon = Icons.Outlined.MapsHomeWork,
        unselectedIcon = Icons.Outlined.MapsHomeWork,
        iconTextId = R.string.tab_home,
        titleTextId = R.string.tab_home,
        route = NavScreen.Home.javaClass.name.replace("$", ".")
    ),
    MAP(
        selectedIcon = Icons.Outlined.Map,
        unselectedIcon = Icons.Outlined.Map,
        iconTextId = R.string.tab_map,
        titleTextId = R.string.tab_map,
        route = NavScreen.Map.javaClass.name.replace("$", ".")
    ),
    LIVE(
        selectedIcon = Icons.Outlined.CameraOutdoor,
        unselectedIcon = Icons.Outlined.CameraOutdoor,
        iconTextId = R.string.tab_live,
        titleTextId = R.string.tab_live,
        route = NavScreen.Live.javaClass.name.replace("$", ".")
    ),
    SEARCH(
        selectedIcon = Icons.Outlined.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.tab_search,
        titleTextId = R.string.tab_search,
        route = NavScreen.Search.javaClass.name.replace("$", ".")
    ),
    PROFILE(
        selectedIcon = Icons.Outlined.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.tab_profile,
        titleTextId = R.string.tab_profile,
        route = NavScreen.Profile.javaClass.name.replace("$", ".")
    )
}