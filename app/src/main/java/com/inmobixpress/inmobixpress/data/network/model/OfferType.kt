package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferType(
    val id: Int,
    val name: String
)