package com.example.build2rise.data.repository

import android.content.Context
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.model.MatchResult
import com.example.build2rise.data.local.TokenManager
import kotlinx.coroutines.flow.first
import retrofit2.Response

class MatchRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(context)

    suspend fun getMatchesForCurrentUser(limit: Int = 20): Response<MatchResult> {
        val token = tokenManager.getToken().first()
        return apiService.getMatchesForCurrentUser(
            token = "Bearer $token",
            limit = limit
        )
    }
}