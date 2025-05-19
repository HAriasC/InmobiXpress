package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Publishing(
    val id: Int,
    val numberView: Int,
    val property: Property
)