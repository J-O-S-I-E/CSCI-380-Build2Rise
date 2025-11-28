package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.build2rise.data.model.UserInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.build2rise.data.api.ApiService


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

data class ShareSearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<UserInfo> = emptyList(),
    val selectedUser: UserInfo? = null,
    val error: String? = null
)


class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _conversationsState = MutableStateFlow<ConversationsState>(ConversationsState.Idle)
    val conversationsState: StateFlow<ConversationsState> = _conversationsState

    private val _conversationDetailState = MutableStateFlow<ConversationDetailState>(ConversationDetailState.Idle)
    val conversationDetailState: StateFlow<ConversationDetailState> = _conversationDetailState

    private val _sendMessageState = MutableStateFlow<SendMessageState>(SendMessageState.Idle)
    val sendMessageState: StateFlow<SendMessageState> = _sendMessageState

    var shareSearchState by mutableStateOf(ShareSearchUiState())
        private set


    private val _sharedPosts = MutableStateFlow<Map<String, PostResponse>>(emptyMap())
    val sharedPosts: StateFlow<Map<String, PostResponse>> = _sharedPosts



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
    fun sendMessage(receiverId: String, content: String, sharedPostId: String? = null ) {
        viewModelScope.launch {
            _sendMessageState.value = SendMessageState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _sendMessageState.value = SendMessageState.Error("Not authenticated")
                    return@launch
                }

                val request = SendMessageRequest(receiverId, content, sharedPostId)
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

    fun sendMessageWithPost(
        receiverId: String,
        sharedPostId: String,
        content: String = "Shared a post with you"
    ) {
        viewModelScope.launch {
            _sendMessageState.value = SendMessageState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _sendMessageState.value = SendMessageState.Error("Not authenticated")
                    return@launch
                }

                // NOTE: this assumes your SendMessageRequest now has a third field: sharedPostId: String?
                val request = SendMessageRequest(
                    receiverId = receiverId,
                    content = content,
                    sharedPostId = sharedPostId
                )

                val response = apiService.sendMessage("Bearer $token", request)

                if (response.isSuccessful) {
                    _sendMessageState.value = SendMessageState.Success("Message sent!")
                    // Refresh the conversation so the shared post message appears
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

    fun onShareSearchQueryChange(newQuery: String) {
        // Update query and clear previous selection
        shareSearchState = shareSearchState.copy(
            query = newQuery,
            selectedUser = null
        )

        if (newQuery.isBlank()) {
            // Clear results if query is empty
            shareSearchState = shareSearchState.copy(
                results = emptyList(),
                error = null
            )
            return
        }

        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    shareSearchState = shareSearchState.copy(
                        isSearching = false,
                        error = "Not authenticated"
                    )
                    return@launch
                }

                // show loading
                shareSearchState = shareSearchState.copy(
                    isSearching = true,
                    error = null
                )

                val response = apiService.searchUsersForMessages(
                    "Bearer $token",
                    newQuery
                )

                if (response.isSuccessful && response.body() != null) {
                    shareSearchState = shareSearchState.copy(
                        isSearching = false,
                        results = response.body()!!,
                        error = null
                    )
                } else {
                    shareSearchState = shareSearchState.copy(
                        isSearching = false,
                        error = "Error searching users"
                    )
                }
            } catch (e: Exception) {
                shareSearchState = shareSearchState.copy(
                    isSearching = false,
                    error = e.message ?: "Network error"
                )
            }
        }
    }

    fun onShareUserSelected(user: UserInfo) {
        shareSearchState = shareSearchState.copy(selectedUser = user)
    }


    fun resetSendState() {
        _sendMessageState.value = SendMessageState.Idle
    }

    fun resetConversationDetail() {
        _conversationDetailState.value = ConversationDetailState.Idle
    }

    fun loadSharedPost(postId: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    return@launch
                }

                val response = apiService.getPostById(
                    "Bearer $token",
                    postId
                )

                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()!!
                    _sharedPosts.value = _sharedPosts.value + (postId to post)
                }
            } catch (e: Exception) {
                // you can log or ignore, it's not critical
            }
        }
    }

}