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
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink

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

    companion object {
        private const val MAX_FILE_SIZE_MB = 50 // 50MB max for videos
        private const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024L
    }

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

    private val _likedPosts = MutableStateFlow<Set<String>>(emptySet())
    val likedPosts = _likedPosts.asStateFlow()

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

    /**
     * Create post with image/video file - WITH FILE SIZE VALIDATION
     */
    fun createPostWithMedia(
        description: String?,
        fileUri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {
            _createPostState.value = CreatePostState.Loading

            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrEmpty()) {
                    _createPostState.value = CreatePostState.Error("Not authenticated")
                    return@launch
                }

                // Check file size BEFORE trying to upload
                if (fileUri != null) {
                    val fileSize = getFileSize(fileUri, context)
                    if (fileSize > MAX_FILE_SIZE_BYTES) {
                        val sizeMB = fileSize / 1024 / 1024
                        _createPostState.value = CreatePostState.Error(
                            "File is too large (${sizeMB}MB). Maximum size is ${MAX_FILE_SIZE_MB}MB. Please compress the video or select a smaller file."
                        )
                        Log.e("POST_UPLOAD", "File too large: ${sizeMB}MB")
                        return@launch
                    }
                    Log.d("POST_UPLOAD", "File size OK: ${fileSize / 1024 / 1024}MB")
                }

                // Convert Uri to MultipartBody.Part using STREAMING
                val filePart = fileUri?.let { uri ->
                    uriToMultipartPart(uri, context)
                }

                // Convert description to RequestBody
                val descriptionPart = description?.toRequestBody("text/plain".toMediaTypeOrNull())

                Log.d("POST_UPLOAD", "Starting upload to server...")
                val response = apiService.createPostWithMedia(
                    token = "Bearer $token",
                    description = descriptionPart,
                    file = filePart
                )

                if (response.isSuccessful) {
                    Log.d("POST_UPLOAD", "Upload successful!")
                    _createPostState.value = CreatePostState.Success("Post created!")
                    getAllPosts() // Refresh feed
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("POST_UPLOAD", "Upload failed: ${response.code()} - $errorMsg")
                    _createPostState.value = CreatePostState.Error("Failed to create post: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("POST_UPLOAD", "Exception during upload", e)
                _createPostState.value = CreatePostState.Error(
                    e.message ?: "Network error"
                )
            }
        }
    }

    /**
     * Helper: Get file size from Uri
     */
    private fun getFileSize(uri: Uri, context: Context): Long {
        return try {
            context.contentResolver.openAssetFileDescriptor(uri, "r")?.use {
                it.length
            } ?: 0L
        } catch (e: Exception) {
            Log.e("POST_UPLOAD", "Error getting file size", e)
            0L
        }
    }

    /**
     * Helper: Convert Android Uri to Retrofit MultipartBody.Part using STREAMING
     * This prevents OutOfMemoryError by NOT loading the entire file into memory
     */
    private suspend fun uriToMultipartPart(uri: Uri, context: Context): MultipartBody.Part? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

                // Get file name
                val fileName = getFileName(uri, contentResolver) ?: "upload_${System.currentTimeMillis()}"

                // Get file size
                val fileSize = getFileSize(uri, context)

                Log.d("POST_UPLOAD", "Preparing to upload: $fileName, size: ${fileSize / 1024 / 1024}MB, type: $mimeType")

                // Create a STREAMING RequestBody that reads the file in chunks
                // This is the KEY FIX - it doesn't load the entire file into memory
                val requestBody = object : okhttp3.RequestBody() {
                    override fun contentType() = mimeType.toMediaTypeOrNull()

                    override fun contentLength() = fileSize

                    override fun writeTo(sink: BufferedSink) {
                        contentResolver.openInputStream(uri)?.use { input ->
                            // Read and write in 8KB chunks
                            val buffer = ByteArray(8 * 1024)
                            var bytesRead: Int
                            var totalBytesWritten = 0L

                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                sink.write(buffer, 0, bytesRead)
                                totalBytesWritten += bytesRead

                                // Log progress every 5MB
                                if (totalBytesWritten % (5 * 1024 * 1024) < buffer.size) {
                                    val progressMB = totalBytesWritten / 1024 / 1024
                                    val totalMB = fileSize / 1024 / 1024
                                    Log.d("POST_UPLOAD", "Upload progress: ${progressMB}MB / ${totalMB}MB")
                                }
                            }

                            Log.d("POST_UPLOAD", "Upload stream complete: ${totalBytesWritten / 1024 / 1024}MB")
                        } ?: throw Exception("Could not open input stream for uri: $uri")
                    }
                }

                MultipartBody.Part.createFormData("file", fileName, requestBody)

            } catch (e: Exception) {
                Log.e("POST_UPLOAD", "Error creating multipart body", e)
                null
            }
        }
    }

    /**
     * Helper: Get file name from Uri
     */
    private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
        var fileName: String? = null

        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }

        return fileName
    }
}