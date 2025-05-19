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
    data class Search(val id: Int = 0)

    @Serializable
    object Profile

    @Serializable
    data class Detail(val id: Int)

    @Serializable
    object Login
}