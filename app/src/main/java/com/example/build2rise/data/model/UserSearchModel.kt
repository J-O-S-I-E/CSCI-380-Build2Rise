package com.example.build2rise.data.model

data class UserSearchResponse(
    val users: List<UserProfileResponse>,
    val totalCount: Int
)