package com.example.build2rise.data.model

// Support Project Request
data class SupportProjectRequest(
    val founderUserId: String,
    val status: String = "interested"
)

// Update Project Status Request
data class UpdateProjectStatusRequest(
    val status: String
)

// Project Response
data class ProjectResponse(
    val id: String,
    val founderId: String,
    val founderName: String?,
    val founderStartupName: String?,
    val founderIndustry: String?,
    val investorId: String,
    val investorName: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

// List of Projects Response
data class ProjectsListResponse(
    val projects: List<ProjectResponse>,
    val totalCount: Int
)