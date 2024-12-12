package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val id: Int,
    val url: String,
    val property: Property
)