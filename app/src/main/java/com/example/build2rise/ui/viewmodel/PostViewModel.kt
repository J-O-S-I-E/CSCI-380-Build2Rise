package com.example.build2rise.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.build2rise.data.api.RetrofitClient
import com.example.build2rise.data.local.TokenManager
import com.example.build2rise.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.build2rise.data.model.AddCommentRequest
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.build2rise.data.model.CommentDto
import kotlinx.coroutines.flow.first









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
data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: List<CommentDto> = emptyList(),
    val error: String? = null,
    val postId: String? = null
)




class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(application)

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState

    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState

    private val _likedPostIds = MutableStateFlow<Set<String>>(emptySet())
    val likedPostIds: StateFlow<Set<String>> = _likedPostIds

    private val _commentsState = MutableStateFlow(CommentsUiState())
    val commentsState: StateFlow<CommentsUiState> = _commentsState.asStateFlow()

    private val _commentsByPostId = MutableStateFlow<Map<String, List<CommentDto>>>(emptyMap())
    val commentsByPostId: StateFlow<Map<String, List<CommentDto>>> = _commentsByPostId.asStateFlow()







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

    private val _likedPosts = MutableStateFlow<Set<String>>(emptySet())
    val likedPosts = _likedPosts.asStateFlow()

    fun sharePost(postId: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    return@launch
                }

                apiService.sharePost(
                    token = "Bearer $token",
                    postId = postId
                )
                // If you ever need updated counts, use response.body()
            } catch (e: Exception) {
                // optional: handle exception
            }
        }
    }



    fun addComment(
        postId: String,
        content: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    return@launch
                }

                val response = apiService.addComment(
                    token = "Bearer $token",
                    postId = postId,
                    request = AddCommentRequest(content = content)
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    // optional: handle error
                }
            } catch (e: Exception) {
                // optional: handle exception
            }
        }
    }
    fun likePost(postId: String?) {
        if (postId == null) return

        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) return@launch

                val response = RetrofitClient.apiService.toggleLike(
                    token = "Bearer $token",
                    postId = postId
                )

                if (response.isSuccessful) {
                    response.body()?.let { interaction ->
                        _likedPostIds.update { current ->
                            if (interaction.likedByCurrentUser) {
                                current + postId
                            } else {
                                current - postId
                            }
                        }
                    }
                } else {
                    Log.e("LIKE_POST", "toggleLike failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LIKE_POST", "toggleLike error", e)
            }
        }
    }

    fun loadPostInteractions(postId: String?) {
        if (postId == null) return

        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) return@launch

                val response = RetrofitClient.apiService.getPostInteractions(
                    token = "Bearer $token",
                    postId = postId
                )

                if (response.isSuccessful) {
                    response.body()?.let { interaction ->
                        _likedPostIds.update { current ->
                            if (interaction.likedByCurrentUser) {
                                current + postId
                            } else {
                                current - postId
                            }
                        }
                    }
                } else {
                    Log.e(
                        "LOAD_INTERACTIONS",
                        "getPostInteractions failed: ${response.code()} ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("LOAD_INTERACTIONS", "getPostInteractions error", e)
            }
        }
    }
    fun loadComments(postId: String?) {
        if (postId == null) return

        viewModelScope.launch {
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) return@launch

                val response = RetrofitClient.apiService.getCommentsForPost(
                    token = "Bearer $token",
                    postId = postId
                )

                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()

                    // Store comments in the map so FeedScreen can show them
                    _commentsByPostId.update { current ->
                        current + (postId to list)
                    }

                    Log.d(
                        "COMMENTS_DEBUG",
                        "postId=$postId, commentsCount=${list.size}"
                    )
                } else {
                    Log.e(
                        "COMMENTS_DEBUG",
                        "Failed to load comments for $postId: ${response.code()} ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("COMMENTS_DEBUG", "Error loading comments for $postId", e)
            }
        }
    }








}
