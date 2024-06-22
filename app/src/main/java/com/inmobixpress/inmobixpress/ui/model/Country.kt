package com.inmobixpress.inmobixpress.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: Int,
    val name: String,
    val countryCode: Int
)
