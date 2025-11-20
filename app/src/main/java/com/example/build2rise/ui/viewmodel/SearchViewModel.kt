package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.UserSearchResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val results: UserSearchResponse) : SearchState()
    data class Error(val message: String) : SearchState()
}

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    fun searchUsers(
        userType: String? = null,
        industry: String? = null,
        location: String? = null,
        fundingStage: String? = null
    ) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _searchState.value = SearchState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.searchUsers(
                    token = "Bearer $token",
                    userType = userType,
                    industry = industry,
                    location = location,
                    fundingStage = fundingStage
                )

                if (response.isSuccessful && response.body() != null) {
                    _searchState.value = SearchState.Success(response.body()!!)
                } else {
                    _searchState.value = SearchState.Error("Failed to search users")
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }
}