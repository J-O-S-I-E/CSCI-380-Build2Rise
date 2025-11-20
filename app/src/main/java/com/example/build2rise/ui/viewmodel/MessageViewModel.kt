package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ConversationsState {
    object Idle : ConversationsState()
    object Loading : ConversationsState()
    data class Success(val conversations: List<ConversationResponse>) : ConversationsState()
    data class Error(val message: String) : ConversationsState()
}

sealed class ConversationDetailState {
    object Idle : ConversationDetailState()
    object Loading : ConversationDetailState()
    data class Success(val conversation: ConversationDetailResponse) : ConversationDetailState()
    data class Error(val message: String) : ConversationDetailState()
}

sealed class SendMessageState {
    object Idle : SendMessageState()
    object Loading : SendMessageState()
    data class Success(val message: String) : SendMessageState()
    data class Error(val message: String) : SendMessageState()
}

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _conversationsState = MutableStateFlow<ConversationsState>(ConversationsState.Idle)
    val conversationsState: StateFlow<ConversationsState> = _conversationsState

    private val _conversationDetailState = MutableStateFlow<ConversationDetailState>(ConversationDetailState.Idle)
    val conversationDetailState: StateFlow<ConversationDetailState> = _conversationDetailState

    private val _sendMessageState = MutableStateFlow<SendMessageState>(SendMessageState.Idle)
    val sendMessageState: StateFlow<SendMessageState> = _sendMessageState

    /**
     * Get all conversations
     */
    fun getConversations() {
        viewModelScope.launch {
            _conversationsState.value = ConversationsState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _conversationsState.value = ConversationsState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getConversations("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    _conversationsState.value = ConversationsState.Success(response.body()!!)
                } else {
                    _conversationsState.value = ConversationsState.Error("Failed to load conversations")
                }
            } catch (e: Exception) {
                _conversationsState.value = ConversationsState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    /**
     * Get conversation with specific user
     */
    fun getConversation(otherUserId: String) {
        viewModelScope.launch {
            _conversationDetailState.value = ConversationDetailState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _conversationDetailState.value = ConversationDetailState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getConversation("Bearer $token", otherUserId)

                if (response.isSuccessful && response.body() != null) {
                    _conversationDetailState.value = ConversationDetailState.Success(response.body()!!)
                } else {
                    _conversationDetailState.value = ConversationDetailState.Error("Failed to load conversation")
                }
            } catch (e: Exception) {
                _conversationDetailState.value = ConversationDetailState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    /**
     * Send a message
     */
    fun sendMessage(receiverId: String, content: String) {
        viewModelScope.launch {
            _sendMessageState.value = SendMessageState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _sendMessageState.value = SendMessageState.Error("Not authenticated")
                    return@launch
                }

                val request = SendMessageRequest(receiverId, content)
                val response = apiService.sendMessage("Bearer $token", request)

                if (response.isSuccessful) {
                    _sendMessageState.value = SendMessageState.Success("Message sent!")
                    // Refresh the conversation to show new message
                    getConversation(receiverId)
                } else {
                    _sendMessageState.value = SendMessageState.Error("Failed to send message")
                }
            } catch (e: Exception) {
                _sendMessageState.value = SendMessageState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun resetSendState() {
        _sendMessageState.value = SendMessageState.Idle
    }

    fun resetConversationDetail() {
        _conversationDetailState.value = ConversationDetailState.Idle
    }
}