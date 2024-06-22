package com.inmobixpress.inmobixpress.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Province(
    val id: Int,
    val name: String,
    val department: Department
)
