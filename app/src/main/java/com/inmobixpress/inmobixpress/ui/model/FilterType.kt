package com.inmobixpress.inmobixpress.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.StoreMallDirectory
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

enum class FilterType(
    val includedTypes: List<String>,
    val label: String,
    val icon: ImageVector
) {
    RESTAURANT(
        includedTypes = listOf("restaurant", "pizza_restaurant"),
        label = "Restaurantes",
        icon = Icons.Outlined.Restaurant
    ),
    EDUCATION(
        includedTypes = listOf("university", "secondary_school","school", "preschool"),
        label = "Educación",
        icon = Icons.Outlined.School
    ),
    SUPERMARKET(
        includedTypes = listOf("supermarket"),
        label = "Surpermercados",
        icon = Icons.Outlined.StoreMallDirectory
    ),
    MALL(
        includedTypes = listOf("shopping_mall"),
        label = "Compras",
        icon = Icons.Outlined.LocalMall
    ),
    MARKET(
        includedTypes = listOf("market"),
        label = "Tiendas",
        icon = Icons.Outlined.Storefront
    ),
    DRUGSTORE(
        includedTypes = listOf("drugstore"),
        label = "Farmacias",
        icon = Icons.Outlined.Medication
    ),
    BAR(
        includedTypes = listOf("bar"),
        label = "Bares",
        icon = Icons.Outlined.LocalBar
    )
}
