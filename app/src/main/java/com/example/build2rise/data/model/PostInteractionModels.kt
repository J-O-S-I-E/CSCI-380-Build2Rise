package com.example.build2rise.data.model


data class PostInteractionResponse(
    val postId: String,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val likedByCurrentUser: Boolean
)

data class AddCommentRequest(
    val content: String
)

data class CommentDto(
    val id: String,
    val userId: String,
    val authorName: String,
    val content: String,
    val createdAt: String
)
/*data class CommentResponse(
    val id: Long,
    val postId: Long,
    val content: String,
    val createdAt: String,
    val authorName: String,
    val authorProfileImageUrl: String? = null
)*/