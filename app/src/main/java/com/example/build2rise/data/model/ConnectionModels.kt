package com.example.build2rise.data.model

data class ConnectionRequest(
    val targetUserId: String
)

data class ConnectionResponse(
    val id: String,
    val user1: ConnectionUserInfo,
    val user2: ConnectionUserInfo,
    val status: String,
    val connectionDate: String
)

data class ConnectionUserInfo(
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val profileImageUrl: String?
)

data class ConnectionsListResponse(
    val connections: List<ConnectionResponse>,
    val totalCount: Int
)

data class UpdateConnectionRequest(
    val status: String
)

