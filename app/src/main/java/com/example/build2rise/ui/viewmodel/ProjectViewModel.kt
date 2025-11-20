package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ProjectsState {
    object Idle : ProjectsState()
    object Loading : ProjectsState()
    data class Success(val projects: ProjectsListResponse) : ProjectsState()
    data class Error(val message: String) : ProjectsState()
}

sealed class ProjectActionState {
    object Idle : ProjectActionState()
    object Loading : ProjectActionState()
    data class Success(val message: String) : ProjectActionState()
    data class Error(val message: String) : ProjectActionState()
}

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _projectsState = MutableStateFlow<ProjectsState>(ProjectsState.Idle)
    val projectsState: StateFlow<ProjectsState> = _projectsState

    private val _projectActionState = MutableStateFlow<ProjectActionState>(ProjectActionState.Idle)
    val projectActionState: StateFlow<ProjectActionState> = _projectActionState

    /**
     * Get investor's supported projects
     */
    fun getInvestorProjects() {
        viewModelScope.launch {
            _projectsState.value = ProjectsState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _projectsState.value = ProjectsState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getInvestorProjects("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    _projectsState.value = ProjectsState.Success(response.body()!!)
                } else {
                    _projectsState.value = ProjectsState.Error("Failed to load projects")
                }
            } catch (e: Exception) {
                _projectsState.value = ProjectsState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    /**
     * Support a founder's project
     */
    fun supportProject(founderUserId: String) {
        viewModelScope.launch {
            _projectActionState.value = ProjectActionState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _projectActionState.value = ProjectActionState.Error("Not authenticated")
                    return@launch
                }

                val request = SupportProjectRequest(founderUserId, "interested")
                val response = apiService.supportProject("Bearer $token", request)

                if (response.isSuccessful) {
                    _projectActionState.value = ProjectActionState.Success("Project supported!")
                    // Refresh projects list
                    getInvestorProjects()
                } else {
                    _projectActionState.value = ProjectActionState.Error("Failed to support project")
                }
            } catch (e: Exception) {
                _projectActionState.value = ProjectActionState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun resetActionState() {
        _projectActionState.value = ProjectActionState.Idle
    }
}