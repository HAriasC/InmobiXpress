package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Department(
    val id: Int,
    val name: String,
    val country: Country
)