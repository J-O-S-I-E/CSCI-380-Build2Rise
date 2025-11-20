package com.example.build2rise.data.model

data class SendMessageRequest(
    val receiverId: String,
    val content: String
)

data class MessageResponse(
    val id: String,
    val senderId: String,
    val senderFirstName: String?,
    val senderLastName: String?,
    val receiverId: String,
    val receiverFirstName: String?,
    val receiverLastName: String?,
    val content: String,
    val readStatus: Boolean,
    val timestamp: String
)

data class ConversationResponse(
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int
)

// Conversation Detail Response (messages with one person)
data class ConversationDetailResponse(
    val otherUser: UserInfo,
    val messages: List<MessageResponse>
)

data class UserInfo(
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val profileImageUrl: String?
)