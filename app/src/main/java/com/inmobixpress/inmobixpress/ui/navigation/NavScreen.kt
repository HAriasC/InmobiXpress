package com.inmobixpress.inmobixpress.ui.navigation

import kotlinx.serialization.Serializable

object NavScreen {

    @Serializable
    object Home

    @Serializable
    object Map

    @Serializable
    object Live

    @Serializable
    object Search

    @Serializable
    object Profile

    @Serializable
    data class Detail(val id: Int)
}