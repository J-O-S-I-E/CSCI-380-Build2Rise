package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val profile: UserProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

sealed class ProfileCreateState {
    object Idle : ProfileCreateState()
    object Loading : ProfileCreateState()
    data class Success(val message: String) : ProfileCreateState()
    data class Error(val message: String) : ProfileCreateState()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    private val _profileCreateState = MutableStateFlow<ProfileCreateState>(ProfileCreateState.Idle)
    val profileCreateState: StateFlow<ProfileCreateState> = _profileCreateState

    fun getCurrentUserProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _profileState.value = ProfileState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getCurrentUserProfile("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    _profileState.value = ProfileState.Success(response.body()!!)
                } else {
                    _profileState.value = ProfileState.Error("Failed to load profile")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _profileState.value = ProfileState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getUserProfile("Bearer $token", userId)

                if (response.isSuccessful && response.body() != null) {
                    _profileState.value = ProfileState.Success(response.body()!!)
                } else {
                    _profileState.value = ProfileState.Error("Failed to load profile")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun createFounderProfile(
        startupName: String,
        industry: String?,
        location: String?,
        teamSize: String?,
        fundingStage: String?,
        description: String?
    ) {
        viewModelScope.launch {
            _profileCreateState.value = ProfileCreateState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _profileCreateState.value = ProfileCreateState.Error("Not authenticated")
                    return@launch
                }

                val request = FounderProfileRequest(
                    startupName, industry, location, teamSize, fundingStage, description
                )

                val response = apiService.createFounderProfile("Bearer $token", request)

                if (response.isSuccessful) {
                    _profileCreateState.value = ProfileCreateState.Success("Profile created successfully!")
                    getCurrentUserProfile() // Refresh profile
                } else {
                    _profileCreateState.value = ProfileCreateState.Error("Failed to create profile")
                }
            } catch (e: Exception) {
                _profileCreateState.value = ProfileCreateState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun createInvestorProfile(
        nameFirm: String,
        industry: String?,
        geographicPreference: String?,
        investmentRange: String?,
        fundingStagePreference: String?
    ) {
        viewModelScope.launch {
            _profileCreateState.value = ProfileCreateState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _profileCreateState.value = ProfileCreateState.Error("Not authenticated")
                    return@launch
                }

                val request = InvestorProfileRequest(
                    nameFirm, industry, geographicPreference, investmentRange, fundingStagePreference
                )

                val response = apiService.createInvestorProfile("Bearer $token", request)

                if (response.isSuccessful) {
                    _profileCreateState.value = ProfileCreateState.Success("Profile created successfully!")
                    getCurrentUserProfile() // Refresh profile
                } else {
                    _profileCreateState.value = ProfileCreateState.Error("Failed to create profile")
                }
            } catch (e: Exception) {
                _profileCreateState.value = ProfileCreateState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun resetCreateState() {
        _profileCreateState.value = ProfileCreateState.Idle
    }
}