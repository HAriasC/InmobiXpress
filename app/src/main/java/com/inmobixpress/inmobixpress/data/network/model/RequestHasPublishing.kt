package com.inmobixpress.inmobixpress.data.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RequestHasPublishing(
    val request: Request,
    val publishing: Publishing,
    val createDate: LocalDateTime
)