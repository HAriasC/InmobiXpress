package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val id: Int,
    val date: LocalDateTime,
    val message: String,
    val requestType: RequestType,
    val requestState: RequestState,
    val user: User
)