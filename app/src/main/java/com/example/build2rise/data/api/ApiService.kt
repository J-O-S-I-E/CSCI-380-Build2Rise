package com.example.build2rise.data.api

import com.example.build2rise.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("auth/health")
    suspend fun healthCheck(): Response<Map<String, String>>

    // User endpoints
    @GET("users/profile")
    suspend fun getCurrentUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>

    @GET("users/{userId}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<UserProfileResponse>

    @POST("users/founder-profile")
    suspend fun createFounderProfile(
        @Header("Authorization") token: String,
        @Body request: FounderProfileRequest
    ): Response<ProfileData>

    @POST("users/investor-profile")
    suspend fun createInvestorProfile(
        @Header("Authorization") token: String,
        @Body request: InvestorProfileRequest
    ): Response<ProfileData>

    @GET("users/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("userType") userType: String? = null,
        @Query("industry") industry: String? = null,
        @Query("location") location: String? = null,
        @Query("fundingStage") fundingStage: String? = null
    ): Response<UserSearchResponse>

    // Post endpoints
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): Response<PostResponse>

    @GET("posts")
    suspend fun getAllPosts(
        @Header("Authorization") token: String
    ): Response<FeedResponse>

    @GET("posts/my-posts")
    suspend fun getCurrentUserPosts(
        @Header("Authorization") token: String
    ): Response<FeedResponse>

    @GET("posts/user/{userId}")
    suspend fun getUserPosts(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<FeedResponse>

    // Message endpoints
    @POST("messages/send")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): Response<MessageResponse>

    @GET("messages/conversations")
    suspend fun getConversations(
        @Header("Authorization") token: String
    ): Response<List<ConversationResponse>>

    @GET("messages/conversation/{userId}")
    suspend fun getConversation(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<ConversationDetailResponse> // ConversationDetailResponse



    // Connection endpoints
    @POST("connections/request")
    suspend fun requestConnection(
        @Header("Authorization") token: String,
        @Body request: ConnectionRequest
    ): Response<ConnectionResponse>

    @GET("connections")
    suspend fun getConnections(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Response<ConnectionsListResponse>

    @PUT("connections/{connectionId}")
    suspend fun updateConnection(
        @Header("Authorization") token: String,
        @Path("connectionId") connectionId: String,
        @Body request: UpdateConnectionRequest
    ): Response<ConnectionResponse>

    // Project endpoints
    @POST("projects/support")
    suspend fun supportProject(
        @Header("Authorization") token: String,
        @Body request: SupportProjectRequest
    ): Response<ProjectResponse>

    @GET("projects/investor")
    suspend fun getInvestorProjects(
        @Header("Authorization") token: String
    ): Response<ProjectsListResponse>

    @GET("projects/founder")
    suspend fun getFounderSupporters(
        @Header("Authorization") token: String
    ): Response<ProjectsListResponse>

    @PUT("projects/{projectId}/status")
    suspend fun updateProjectStatus(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: String,
        @Body request: UpdateProjectStatusRequest
    ): Response<ProjectResponse>

    @GET("projects/check/{founderUserId}")
    suspend fun checkSupporting(
        @Header("Authorization") token: String,
        @Path("founderUserId") founderUserId: String
    ): Response<Map<String, Boolean>>
}