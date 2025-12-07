package com.example.build2rise.data.model

data class FounderMatchDTO(
    val founderId: String,
    val founderUserId: String,
    val startupName: String,
    val industry: String,
    val location: String,
    val fundingStage: String,
    val description: String,
    val teamSize: String?,
    val matchScore: Int,
    val matchReasons: List<String>
)

data class InvestorMatchDTO(
    val investorId: String,
    val investorUserId: String,
    val nameFirm: String,
    val industry: String,
    val geographicPreference: String,
    val fundingStagePreference: String,
    val investmentRange: String,
    val matchScore: Int,
    val matchReasons: List<String>
)

data class MatchResult(
    val matches: List<Map<String, Any>>,
    val totalMatches: Int,
    val averageScore: Double
)