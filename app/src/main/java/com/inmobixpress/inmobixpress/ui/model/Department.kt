package com.inmobixpress.inmobixpress.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Department(
    val id: Int,
    val name: String,
    val country: Country
)
