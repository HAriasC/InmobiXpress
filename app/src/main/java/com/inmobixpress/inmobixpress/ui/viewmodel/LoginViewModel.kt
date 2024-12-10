package com.inmobixpress.inmobixpress.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmobixpress.inmobixpress.data.network.model.User
import com.inmobixpress.inmobixpress.repository.LoginRepository
import com.inmobixpress.inmobixpress.ui.model.UIState
import com.inmobixpress.inmobixpress.ui.model.UIState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _loadingVisible = MutableLiveData<Boolean>()
    val loadingVisible: LiveData<Boolean> = _loadingVisible

    private val _errorDialogVisible = MutableLiveData<Boolean>()
    val errorDialogVisible: LiveData<Boolean> = _errorDialogVisible

    private val _usernameError = MutableLiveData<Boolean>()
    val usernameError: LiveData<Boolean> = _usernameError

    private val _usernameMessageError = MutableLiveData<String>()
    val usernameMessageError: LiveData<String> = _usernameMessageError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean> = _passwordError

    private val _passwordMessageError = MutableLiveData<String>()
    val passwordMessageError: LiveData<String> = _passwordMessageError

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _result = MutableStateFlow<UIState<User>>(None())
    val result = _result.asStateFlow()

    fun onLoadingVisible(visible: Boolean) {
        _loadingVisible.value = visible
    }

    fun onErrorDialogVisible(visible: Boolean) {
        _errorDialogVisible.value = visible
    }

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun validateUsername(): Boolean {
        if (_username.value.isNullOrBlank()) {
            _usernameMessageError.value = "Ingresa tu nombre de usuario"
            _usernameError.value = true
        } else {
            _usernameError.value = false
        }
        return _usernameError.value == false
    }

    fun validatePassword(): Boolean {
        if (_password.value.isNullOrBlank()) {
            _passwordMessageError.value = "Ingresa tu contraseña"
            _passwordError.value = true
        } else {
            _passwordError.value = false
        }
        return _passwordError.value == false
    }

    fun validateForm(): Boolean {
        if (validateUsername() && validatePassword()) {
            //_loadingVisible.value = true
            return true
        } else {
            //_errorDialogVisible.value = true
            return false
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login(
                username = _username.value.toString(),
                password = _password.value.toString()
            )
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    Log.e("REPV", it.message.toString())
                    _result.value = Error(error = it) }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                    Loading()
                ).collect { _result.value = it }
        }
    }

    fun reset() {
        _result.value = None()
    }

    fun clearForm() {
        onUsernameChanged(username = "")
        onPasswordChanged(password = "")
        _usernameError.value = false
        _passwordError.value = false
    }
}