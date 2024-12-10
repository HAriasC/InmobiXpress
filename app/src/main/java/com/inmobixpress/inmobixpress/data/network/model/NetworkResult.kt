package com.inmobixpress.inmobixpress.data.network.model

sealed interface NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>
    data class Error<out T : Any>(val error: Exception) : NetworkResult<T>
    class Loading<T : Any> : NetworkResult<T>
}