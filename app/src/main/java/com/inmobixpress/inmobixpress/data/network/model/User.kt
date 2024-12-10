package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val motherLastName: String,
    val businessName: String,
    val email: String,
    val identityDocument: String,
    val username: String,
    val password: String,
    val documentType: DocumentType,
    val country: Country
)
