package com.example.build2rise.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val userType: String // "founder" or "investor"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val message: String
)