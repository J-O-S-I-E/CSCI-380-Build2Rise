package com.example.build2rise.data.model

data class CreatePostRequest(
    val postDescription: String?,
    val postType: String = "text",
    val mediaUrl: String? = null
)

data class PostResponse(
    val id: String,
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val postDescription: String?,
    val postType: String,
    val mediaUrl: String?,
    val postingDate: String,
    val createdAt: String,
    val likedByCurrentUser: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val shareCount: Int = 0

)

data class FeedResponse(
    val posts: List<PostResponse>,
    val totalCount: Int
)
