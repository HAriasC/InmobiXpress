package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val altitudeBase: Double
) : Parcelable
