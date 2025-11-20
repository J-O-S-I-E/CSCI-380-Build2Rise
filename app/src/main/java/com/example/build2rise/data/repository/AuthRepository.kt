package com.example.build2rise.data.repository

import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.model.AuthResponse
import com.example.build2rise.data.model.LoginRequest
import com.example.build2rise.data.model.RegisterRequest
import retrofit2.Response

class AuthRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return apiService.register(request)
    }

    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }
}
