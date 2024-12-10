package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: Int,
    val phone: String,
    val token: String,
    val user: User
)