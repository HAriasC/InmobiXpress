package com.inmobixpress.inmobixpress.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Proprietor(
    val id: Int,
    val name: String,
    val lastName: String,
    val motherLastName: String,
    val phone : Int,
    val email: String
)
