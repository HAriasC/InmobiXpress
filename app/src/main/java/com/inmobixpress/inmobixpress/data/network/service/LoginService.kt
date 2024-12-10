package com.inmobixpress.inmobixpress.data.network.service

import com.inmobixpress.inmobixpress.data.network.model.NetworkResult
import com.inmobixpress.inmobixpress.data.network.model.User
import kotlinx.coroutines.flow.Flow

interface LoginService {
    fun login(username: String, password: String): Flow<NetworkResult<User>>
}