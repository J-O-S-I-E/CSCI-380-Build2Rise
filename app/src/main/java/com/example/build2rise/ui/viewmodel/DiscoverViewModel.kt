package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.model.FounderMatchDTO
import com.example.build2rise.data.model.InvestorMatchDTO
import com.example.build2rise.data.repository.MatchRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DiscoverUiState {
    object Loading : DiscoverUiState()
    data class FounderMatches(
        val matches: List<FounderMatchDTO>,
        val totalMatches: Int,
        val averageScore: Double
    ) : DiscoverUiState()
    data class InvestorMatches(
        val matches: List<InvestorMatchDTO>,
        val totalMatches: Int,
        val averageScore: Double
    ) : DiscoverUiState()
    data class Error(val message: String) : DiscoverUiState()
}

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    private val matchRepository = MatchRepository(application)
    private val gson = Gson()

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    fun loadMatches(userType: String) {
        viewModelScope.launch {
            _uiState.value = DiscoverUiState.Loading

            try {
                val response = matchRepository.getMatchesForCurrentUser()

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    // Convert generic map list to typed DTOs
                    val normalizedType = userType.trim().lowercase()

                    if (normalizedType == "investor") {
                        // Investor sees founder matches
                        val founderMatches = result.matches.map { matchMap ->
                            gson.fromJson(gson.toJson(matchMap), FounderMatchDTO::class.java)
                        }

                        _uiState.value = DiscoverUiState.FounderMatches(
                            matches = founderMatches,
                            totalMatches = result.totalMatches,
                            averageScore = result.averageScore
                        )
                    } else {
                        // Founder sees investor matches
                        val investorMatches = result.matches.map { matchMap ->
                            gson.fromJson(gson.toJson(matchMap), InvestorMatchDTO::class.java)
                        }

                        _uiState.value = DiscoverUiState.InvestorMatches(
                            matches = investorMatches,
                            totalMatches = result.totalMatches,
                            averageScore = result.averageScore
                        )
                    }
                } else {
                    _uiState.value = DiscoverUiState.Error("Failed to load matches: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("DiscoverViewModel", "Error loading matches", e)
                _uiState.value = DiscoverUiState.Error(e.message ?: "Network error")
            }
        }
    }
}