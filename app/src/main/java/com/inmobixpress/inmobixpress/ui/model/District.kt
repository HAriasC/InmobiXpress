package com.inmobixpress.inmobixpress.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class District(
    val id: Int,
    val name: String,
    val province: Province
)
