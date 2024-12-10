package com.inmobixpress.inmobixpress.ui.model

sealed interface MainUIState {
    data object Loading : MainUIState
    data object Success : MainUIState
}