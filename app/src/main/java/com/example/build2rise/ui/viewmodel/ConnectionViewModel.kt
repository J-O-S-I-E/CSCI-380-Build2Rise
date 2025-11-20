package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ConnectionsState {
    object Idle : ConnectionsState()
    object Loading : ConnectionsState()
    data class Success(val connections: ConnectionsListResponse) : ConnectionsState()
    data class Error(val message: String) : ConnectionsState()
}

sealed class RequestsState {
    object Idle : RequestsState()
    object Loading : RequestsState()
    data class Success(val requests: ConnectionsListResponse) : RequestsState()
    data class Error(val message: String) : RequestsState()
}

sealed class ConnectionActionState {
    object Idle : ConnectionActionState()
    object Loading : ConnectionActionState()
    data class Success(val message: String) : ConnectionActionState()
    data class Error(val message: String) : ConnectionActionState()
}

class ConnectionViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _connectionsState = MutableStateFlow<ConnectionsState>(ConnectionsState.Idle)
    val connectionsState: StateFlow<ConnectionsState> = _connectionsState

    private val _requestsState = MutableStateFlow<RequestsState>(RequestsState.Idle)
    val requestsState: StateFlow<RequestsState> = _requestsState

    private val _connectionActionState = MutableStateFlow<ConnectionActionState>(ConnectionActionState.Idle)
    val connectionActionState: StateFlow<ConnectionActionState> = _connectionActionState

    /**
     * Get accepted connections
     */
    fun getConnections() {
        viewModelScope.launch {
            _connectionsState.value = ConnectionsState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _connectionsState.value = ConnectionsState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getConnections("Bearer $token", status = "accepted")

                if (response.isSuccessful && response.body() != null) {
                    _connectionsState.value = ConnectionsState.Success(response.body()!!)
                } else {
                    _connectionsState.value = ConnectionsState.Error("Failed to load connections")
                }
            } catch (e: Exception) {
                _connectionsState.value = ConnectionsState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    /**
     * Get pending connection requests
     */
    fun getPendingRequests() {
        viewModelScope.launch {
            _requestsState.value = RequestsState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _requestsState.value = RequestsState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getConnections("Bearer $token", status = "pending")

                if (response.isSuccessful && response.body() != null) {
                    _requestsState.value = RequestsState.Success(response.body()!!)
                } else {
                    _requestsState.value = RequestsState.Error("Failed to load requests")
                }
            } catch (e: Exception) {
                _requestsState.value = RequestsState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun requestConnection(targetUserId: String) {
        viewModelScope.launch {
            _connectionActionState.value = ConnectionActionState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _connectionActionState.value = ConnectionActionState.Error("Not authenticated")
                    return@launch
                }

                val request = ConnectionRequest(targetUserId)
                val response = apiService.requestConnection("Bearer $token", request)

                if (response.isSuccessful) {
                    _connectionActionState.value = ConnectionActionState.Success("Connection requested!")
                    getConnections()
                } else {
                    _connectionActionState.value = ConnectionActionState.Error("Failed to send request")
                }
            } catch (e: Exception) {
                _connectionActionState.value = ConnectionActionState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun acceptConnection(connectionId: String) {
        viewModelScope.launch {
            _connectionActionState.value = ConnectionActionState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _connectionActionState.value = ConnectionActionState.Error("Not authenticated")
                    return@launch
                }

                val request = UpdateConnectionRequest("accepted")
                val response = apiService.updateConnection("Bearer $token", connectionId, request)

                if (response.isSuccessful) {
                    _connectionActionState.value = ConnectionActionState.Success("Connection accepted!")
                    getPendingRequests()
                    getConnections()
                } else {
                    _connectionActionState.value = ConnectionActionState.Error("Failed to accept")
                }
            } catch (e: Exception) {
                _connectionActionState.value = ConnectionActionState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun rejectConnection(connectionId: String) {
        viewModelScope.launch {
            _connectionActionState.value = ConnectionActionState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _connectionActionState.value = ConnectionActionState.Error("Not authenticated")
                    return@launch
                }

                val request = UpdateConnectionRequest("rejected")
                val response = apiService.updateConnection("Bearer $token", connectionId, request)

                if (response.isSuccessful) {
                    _connectionActionState.value = ConnectionActionState.Success("Connection rejected")
                    getPendingRequests()
                } else {
                    _connectionActionState.value = ConnectionActionState.Error("Failed to reject")
                }
            } catch (e: Exception) {
                _connectionActionState.value = ConnectionActionState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun resetActionState() {
        _connectionActionState.value = ConnectionActionState.Idle
    }
}