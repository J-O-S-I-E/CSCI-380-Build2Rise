package com.example.build2rise.data.model

data class FounderProfileRequest(
    val startupName: String,
    val industry: String?,
    val location: String?,
    val teamSize: String?,
    val fundingStage: String?,
    val description: String?
)

data class InvestorProfileRequest(
    val nameFirm: String,
    val industry: String?,
    val geographicPreference: String?,
    val investmentRange: String?,
    val fundingStagePreference: String?
)

data class UserProfileResponse(
    val userId: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val profileImageUrl: String?,
    val createdAt: String,
    val profileData: ProfileData?
)

data class ProfileData(
    val startupName: String? = null,
    val teamSize: String? = null,
    val fundingStage: String? = null,
    val nameFirm: String? = null,
    val investmentRange: String? = null,
    val fundingStagePreference: String? = null,
    val industry: String? = null,
    val location: String? = null,
    val description: String? = null
)