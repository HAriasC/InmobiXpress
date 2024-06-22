package com.inmobixpress.inmobixpress.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.graphics.vector.ImageVector
import com.inmobixpress.inmobixpress.R

enum class PropertyType(
    val typeId: Int,
    val icon: ImageVector
) {
    APARTMENT(
        typeId = R.string.item_apartment,
        icon = Icons.Outlined.Apartment
    ),
    HOUSE(
        typeId = R.string.item_house,
        icon = Icons.Outlined.House
    ),
    SHOP(
        typeId = R.string.item_shop,
        icon = Icons.Outlined.Store
    )
}
