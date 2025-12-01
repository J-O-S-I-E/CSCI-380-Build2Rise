package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.ProfileState
import com.example.build2rise.ui.viewmodel.PostViewModel
import com.example.build2rise.ui.viewmodel.PostState

/**
 * Screen to view another user's profile
 * Displays their info, posts, and allows messaging/connecting
 */
@Composable
fun OtherUserProfileScreen(
    userId: String,
    onBack: () -> Unit,
    onMessageClick: (String, String) -> Unit,
    profileViewModel: ProfileViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    val profileState by profileViewModel.profileState.collectAsState()
    val postState by postViewModel.postState.collectAsState()

    // Load user's profile and posts
    LaunchedEffect(userId) {
        profileViewModel.getUserProfile(userId)
        postViewModel.getUserPosts(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RussianViolet
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
            }
        }

        when (val state = profileState) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RussianViolet)
                }
            }

            is ProfileState.Success -> {
                val profile = state.profile

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Profile header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile picture
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(RussianViolet),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = "${profile.firstName?.firstOrNull() ?: ""}${profile.lastName?.firstOrNull() ?: ""}"
                            Text(
                                text = initials.ifEmpty { "U" },
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Name/Firm/Startup
                        Text(
                            text = when (profile.userType) {
                                "founder" -> profile.profileData?.startupName ?: "${profile.firstName} ${profile.lastName}"
                                "investor" -> profile.profileData?.nameFirm ?: "${profile.firstName} ${profile.lastName}"
                                else -> "${profile.firstName} ${profile.lastName}"
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )

                        Text(
                            text = profile.userType.replaceFirstChar { it.uppercase() },
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Message button
                        Button(
                            onClick = {
                                val userName = when (profile.userType) {
                                    "founder" -> profile.profileData?.startupName ?: "${profile.firstName} ${profile.lastName}"
                                    "investor" -> profile.profileData?.nameFirm ?: "${profile.firstName} ${profile.lastName}"
                                    else -> "${profile.firstName} ${profile.lastName}"
                                }
                                onMessageClick(userId, userName)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RussianViolet
                            ),
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Message,
                                contentDescription = "Message",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Message")
                        }

                        // About section
                        val bioText = when (profile.userType) {
                            "founder" -> profile.profileData?.description
                            "investor" -> buildString {
                                profile.profileData?.nameFirm?.let { append("$it\n\n") }
                                val details = mutableListOf<String>()
                                profile.profileData?.industry?.let { details.add("Industry: $it") }
                                profile.profileData?.fundingStagePreference?.let { details.add("Focus: $it") }
                                profile.profileData?.investmentRange?.let { details.add("Investment Range: $it") }
                                if (details.isNotEmpty()) {
                                    append(details.joinToString("\n"))
                                }
                            }.trim().ifBlank { null }
                            else -> null
                        }

                        if (!bioText.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Almond.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "About",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RussianViolet
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = bioText,
                                        fontSize = 14.sp,
                                        color = RussianViolet.copy(alpha = 0.8f),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tab header
                        Text(
                            text = "Posts",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Almond, thickness = 1.dp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Posts grid
                    when (val posts = postState) {
                        is PostState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = RussianViolet)
                            }
                        }

                        is PostState.Success -> {
                            val userPosts = posts.feed.posts

                            if (userPosts.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No posts yet",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp)
                                ) {
                                    items(userPosts) { post ->
                                        if (profile.userType == "founder") {
                                            FounderPostCard(post = post)
                                        } else {
                                            PostCard(post = post)
                                        }
                                    }
                                }
                            }
                        }

                        is PostState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Error loading posts", color = Color.Red)
                                    Button(
                                        onClick = { postViewModel.getUserPosts(userId) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = RussianViolet
                                        )
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }

            is ProfileState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                        Button(
                            onClick = { profileViewModel.getUserProfile(userId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RussianViolet
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}