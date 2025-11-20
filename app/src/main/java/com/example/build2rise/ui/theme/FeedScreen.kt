
package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.build2rise.ui.viewmodel.PostViewModel
import com.example.build2rise.ui.viewmodel.PostState
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.ProfileState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    postViewModel: PostViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Founders", "Investors")
    var showPostModal by remember { mutableStateOf(false) }

    val postState by postViewModel.postState.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()

    // Load posts and profile on screen open
    LaunchedEffect(Unit) {
        postViewModel.getAllPosts()
        profileViewModel.getCurrentUserProfile()
    }

    // Get user initials from profile
    val userInitials = when (val state = profileState) {
        is ProfileState.Success -> {
            val profile = state.profile
            "${profile.firstName?.firstOrNull() ?: ""}${profile.lastName?.firstOrNull() ?: ""}".ifEmpty { "U" }
        }
        else -> "U"
    }

    // Show post creation modal
    if (showPostModal) {
        PostCreationModal(
            userInitials = userInitials,
            onDismiss = { showPostModal = false },
            onPost = { description, mediaType ->
                postViewModel.createPost(description)
                showPostModal = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Top Bar with + button
        FeedTopBar(
            onAddClick = { showPostModal = true }
        )

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = PureWhite,
            contentColor = RussianViolet
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }
                )
            }
        }

        // Feed Content based on state
        when (val state = postState) {
            is PostState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RussianViolet)
                }
            }

            is PostState.Error -> {
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
                            onClick = { postViewModel.getAllPosts() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RussianViolet
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is PostState.Success -> {
                val filteredPosts = state.feed.posts.filter { post ->
                    if (selectedTab == 0) {
                        post.userType == "founder"
                    } else {
                        post.userType == "investor"
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Share Update Section (only show on Founders tab)
//                    if (selectedTab == 0) {
//                        item {
//                            ShareUpdateCard(
//                                userInitials = userInitials,
//                                onClick = { showPostModal = true }
//                            )
//                        }
//                    }

                    // Posts
                    items(filteredPosts) { post ->
                        RealPostCard(
                            post = post,
                            onLike = { /* TODO: Implement like functionality */ },
                            onComment = { /* TODO: Implement comment functionality */ },
                            onShare = { /* TODO: Implement share functionality */ }
                        )
                    }

                    // Show message if no posts
                    if (filteredPosts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No posts yet. Be the first to share!",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                // Idle state - show empty
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun FeedTopBar(
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Build2Rise",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = RussianViolet
        )

        // + Button
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Create Post",
                tint = RussianViolet,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
//
//@Composable
//fun ShareUpdateCard(
//    userInitials: String,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Almond),
//        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            // Profile + Input Row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // User Avatar with real initials
//                Box(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(CircleShape)
//                        .background(RussianViolet),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = userInitials,
//                        color = PureWhite,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//                }
//
//                // Input Field
//                Row(
//                    modifier = Modifier
//                        .weight(1f)
//                        .clip(RoundedCornerShape(24.dp))
//                        .background(PureWhite)
//                        .clickable(onClick = onClick)
//                        .padding(horizontal = 16.dp, vertical = 12.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Share an update",
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//                    Icon(
//                        imageVector = Icons.Default.Send,
//                        contentDescription = "Send",
//                        tint = RussianViolet,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun RealPostCard(
    post: com.example.build2rise.data.model.PostResponse,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Almond)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar with initials
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(RussianViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = "${post.firstName?.firstOrNull() ?: ""}${post.lastName?.firstOrNull() ?: ""}"
                        Text(
                            text = initials.ifEmpty { "U" },
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Author Info
                    Column {
                        Text(
                            text = "${post.firstName ?: ""} ${post.lastName ?: ""}".trim()
                                .ifEmpty { "User" },
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RussianViolet
                        )
                        Text(
                            text = "${post.userType.replaceFirstChar { it.uppercase() }}, ${formatTimestamp(post.createdAt)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Action Icon
                IconButton(onClick = { /* More options */ }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More",
                        tint = RussianViolet,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Content
            if (post.postDescription != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.postDescription,
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }

            // Media placeholder if post has media
            if (post.mediaUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Almond),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.postType,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // Engagement Actions
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Almond, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EngagementButton(
                    icon = Icons.Outlined.ThumbUp,
                    label = "Like",
                    onClick = onLike
                )
                EngagementButton(
                    icon = Icons.Outlined.Comment,
                    label = "Comment",
                    onClick = onComment
                )
                EngagementButton(
                    icon = Icons.Outlined.Share,
                    label = "Share",
                    onClick = onShare
                )
            }
        }
    }
}

@Composable
fun EngagementButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = RussianViolet.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = RussianViolet.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val now = Instant.now()
        val minutes = ChronoUnit.MINUTES.between(instant, now)

        when {
            minutes < 1 -> "just now"
            minutes < 60 -> "${minutes}m ago"
            minutes < 1440 -> "${minutes / 60}h ago"
            minutes < 10080 -> "${minutes / 1440}d ago"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("MMM dd")
                    .withZone(ZoneId.systemDefault())
                formatter.format(instant)
            }
        }
    } catch (e: Exception) {
        "recently"
    }
}