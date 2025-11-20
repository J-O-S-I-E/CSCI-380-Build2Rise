package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.LoginRequest
import com.example.build2rise.data.model.RegisterRequest
import com.example.build2rise.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String, val userType: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val tokenManager = TokenManager(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val token = tokenManager.getToken().first()
            _isLoggedIn.value = !token.isNullOrEmpty()
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        userType: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = repository.register(
                    RegisterRequest(email, password, firstName, lastName, userType)
                )

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    // Save token and user info
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUserId(authResponse.userId)
                    tokenManager.saveUserType(authResponse.userType)

                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success(
                        authResponse.message,
                        authResponse.userType
                    )
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Registration failed"
                    _authState.value = AuthState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Network error. Please check your connection."
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = repository.login(LoginRequest(email, password))

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    // Save token and user info
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUserId(authResponse.userId)
                    tokenManager.saveUserType(authResponse.userType)

                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success(
                        authResponse.message,
                        authResponse.userType
                    )
                } else {
                    _authState.value = AuthState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Network error. Please check your connection."
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearAll()
            _isLoggedIn.value = false
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    suspend fun getToken(): String? {
        return tokenManager.getToken().first()
    }

    suspend fun getUserType(): String? {
        return tokenManager.getUserType().first()
    }
}