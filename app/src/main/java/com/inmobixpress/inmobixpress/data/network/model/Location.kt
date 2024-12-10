package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val altitudeBase: Double
)