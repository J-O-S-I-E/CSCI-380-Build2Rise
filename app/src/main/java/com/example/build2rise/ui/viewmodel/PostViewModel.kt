package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PostState {
    object Idle : PostState()
    object Loading : PostState()
    data class Success(val feed: FeedResponse) : PostState()
    data class Error(val message: String) : PostState()
}

sealed class CreatePostState {
    object Idle : CreatePostState()
    object Loading : CreatePostState()
    data class Success(val message: String) : CreatePostState()
    data class Error(val message: String) : CreatePostState()
}

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState

    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState

    fun getAllPosts() {
        viewModelScope.launch {
            _postState.value = PostState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _postState.value = PostState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getAllPosts("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    _postState.value = PostState.Success(response.body()!!)
                } else {
                    _postState.value = PostState.Error("Failed to load posts")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun getCurrentUserPosts() {
        viewModelScope.launch {
            _postState.value = PostState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _postState.value = PostState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getCurrentUserPosts("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    _postState.value = PostState.Success(response.body()!!)
                } else {
                    _postState.value = PostState.Error("Failed to load posts")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun getUserPosts(userId: String) {
        viewModelScope.launch {
            _postState.value = PostState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _postState.value = PostState.Error("Not authenticated")
                    return@launch
                }

                val response = apiService.getUserPosts("Bearer $token", userId)

                if (response.isSuccessful && response.body() != null) {
                    _postState.value = PostState.Success(response.body()!!)
                } else {
                    _postState.value = PostState.Error("Failed to load posts")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun createPost(description: String, postType: String = "text", mediaUrl: String? = null) {
        viewModelScope.launch {
            _createPostState.value = CreatePostState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _createPostState.value = CreatePostState.Error("Not authenticated")
                    return@launch
                }

                val request = CreatePostRequest(description, postType, mediaUrl)
                val response = apiService.createPost("Bearer $token", request)

                if (response.isSuccessful) {
                    _createPostState.value = CreatePostState.Success("Post created!")
                    getAllPosts() // Refresh feed
                } else {
                    _createPostState.value = CreatePostState.Error("Failed to create post")
                }
            } catch (e: Exception) {
                _createPostState.value = CreatePostState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    fun resetCreateState() {
        _createPostState.value = CreatePostState.Idle
    }
}