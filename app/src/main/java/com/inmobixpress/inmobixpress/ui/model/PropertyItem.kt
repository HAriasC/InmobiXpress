package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyItem(
    val id: Int,
    val price: Int,
    val maintenance: Int,
    val address: String,
    val postalCode: String,
    val description: String,
    val area: Double,
    val bedrooms: Int,
    val bathrooms: Double,
    val garages: Int,
    val type: PropertyType,
    val proprietor: Proprietor,
    val intermediary: Intermediary,
    val district: District,
    val location: Location,
    val images: List<String>
) : Parcelable
