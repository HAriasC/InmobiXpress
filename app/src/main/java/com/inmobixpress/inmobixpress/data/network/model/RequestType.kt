package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestType(
    val id: Int,
    val name: String
)