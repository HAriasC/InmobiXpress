package com.inmobixpress.inmobixpress.ui.model

sealed interface UIState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UIState<T>
    data class Error<out T : Any>(val error: Throwable) : UIState<T>
    class Loading<T : Any> : UIState<T>
    class None<T : Any> : UIState<T>
}