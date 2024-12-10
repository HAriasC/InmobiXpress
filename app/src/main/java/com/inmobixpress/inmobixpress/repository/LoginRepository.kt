package com.inmobixpress.inmobixpress.repository

import com.inmobixpress.inmobixpress.data.network.model.NetworkResult
import com.inmobixpress.inmobixpress.data.network.model.User
import com.inmobixpress.inmobixpress.data.network.service.LoginService
import com.inmobixpress.inmobixpress.ui.model.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginService: LoginService) {

    fun login(username: String, password: String): Flow<UIState<User>> {
        return loginService.login(username, password).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
}