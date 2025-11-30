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
import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.build2rise.data.model.CommentDto
import com.example.build2rise.ui.viewmodel.MessageViewModel
import com.example.build2rise.data.model.PostResponse
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    postViewModel: PostViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    messageViewModel: MessageViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Founders", "Investors")
    var showPostModal by remember { mutableStateOf(false) }

    var showCommentDialog by remember { mutableStateOf(false) }
    var selectedPostIdForComment by remember { mutableStateOf<String?>(null) }
    var commentText by remember { mutableStateOf("") }

    var showShareDialog by remember { mutableStateOf(false) }
    var selectedPostForShare by remember { mutableStateOf<PostResponse?>(null) }

    val context = LocalContext.current

    if (showCommentDialog) {
        AddCommentDialog(
            commentText = commentText,
            onCommentChange = { commentText = it },
            onCancel = {
                showCommentDialog = false
                commentText = ""
            },
            onPost = {
                Log.d("COMMENT_DIALOG", "selectedPostIdForComment = $selectedPostIdForComment")

                val postId = selectedPostIdForComment ?: return@AddCommentDialog
                postViewModel.addComment(
                    postId = postId,
                    content = commentText,
                    onSuccess = {
                        showCommentDialog = false
                        commentText = ""
                        postViewModel.loadComments(postId)
                    }
                )
            }
        )
    }

    val postState by postViewModel.postState.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()
    val likedPosts by postViewModel.likedPosts.collectAsState()
    val likedPostIds by postViewModel.likedPostIds.collectAsState()
    val commentsByPostId by postViewModel.commentsByPostId.collectAsState()

    val conversationsState by messageViewModel.conversationsState.collectAsState()

    val conversations = when (val s = conversationsState) {
        is com.example.build2rise.ui.viewmodel.ConversationsState.Success -> s.conversations
        else -> emptyList()
    }

    // Load posts and profile on screen open
    LaunchedEffect(Unit) {
        postViewModel.getAllPosts()
        profileViewModel.getCurrentUserProfile()
        messageViewModel.getConversations()
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
            onPost = { description, fileUri ->
                if (fileUri != null) {
                    // Upload with media
                    postViewModel.createPostWithMedia(
                        description = description.ifBlank { null },
                        fileUri = fileUri,
                        context = context
                    )
                } else {
                    // Text-only post
                    postViewModel.createPost(description)
                }
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
                LaunchedEffect(state.feed.posts) {
                    state.feed.posts.forEach { post ->
                        post.id?.let { postId ->
                            postViewModel.loadComments(postId)
                            postViewModel.loadPostInteractions(postId)
                        }
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
                    // Posts
                    items(filteredPosts) { post ->
                        val commentsForPost = commentsByPostId[post.id] ?: emptyList()
                        RealPostCard(
                            post = post,
                            isLiked = likedPostIds.contains(post.id),
                            comments = commentsForPost,
                            onLike = { postViewModel.likePost(post.id) },
                            onComment = {
                                selectedPostIdForComment = post.id
                                showCommentDialog = true
                                postViewModel.loadComments(post.id)
                            },
                            onShare = {
                                selectedPostForShare = post
                                showShareDialog = true
                            }
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

        // Share via message dialog
        if (showShareDialog && selectedPostForShare != null) {
            SharePostDialogNew(
                messageViewModel = messageViewModel,
                post = selectedPostForShare!!,
                onDismiss = { showShareDialog = false },
                onShared = {
                    // bump share count in backend when successfully shared
                    val postId = selectedPostForShare!!.id
                    if (postId != null) {
                        postViewModel.sharePost(postId)
                    }
                }
            )
        }
    }
}

@Composable
fun AddCommentDialog(
    commentText: String,
    onCommentChange: (String) -> Unit,
    onCancel: () -> Unit,
    onPost: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Almond)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add Comment",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                )

                TextField(
                    value = commentText,
                    onValueChange = onCommentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.5.dp,
                            color = RussianViolet,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Almond),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Almond,
                        unfocusedContainerColor = Almond,
                        disabledContainerColor = Almond,
                        cursorColor = RussianViolet,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Share your thoughts",
                            color = RussianViolet.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        color = RussianViolet,
                        modifier = Modifier.clickable { onCancel() }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(RussianViolet)
                            .clickable { onPost() }
                            .padding(horizontal = 28.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Post",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SharePostDialogNew(
    messageViewModel: MessageViewModel,
    post: PostResponse,
    onDismiss: () -> Unit,
    onShared: () -> Unit
) {
    val state = messageViewModel.shareSearchState
    val sendEnabled = state.selectedUser != null && post.id != null

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Almond)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Share post via message",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )

                Text(
                    text = post.postDescription ?: "[No description]",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.8f)
                )

                TextField(
                    value = state.query,
                    onValueChange = { messageViewModel.onShareSearchQueryChange(it) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.5.dp,
                            color = RussianViolet,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Almond),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Almond,
                        unfocusedContainerColor = Almond,
                        disabledContainerColor = Almond,
                        cursorColor = RussianViolet,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Search users...",
                            color = RussianViolet.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    }
                )

                if (state.isSearching) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(20.dp),
                            color = RussianViolet
                        )
                    }
                }

                if (state.error != null) {
                    Text(
                        text = state.error,
                        fontSize = 13.sp,
                        color = Color.Red
                    )
                }

                if (state.results.isEmpty() && state.query.isNotBlank() && !state.isSearching) {
                    Text(
                        text = "No users found.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                } else if (state.results.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(state.results) { user ->
                            val isSelected = state.selectedUser?.userId == user.userId

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Almond.copy(alpha = 0.7f) else Color.Transparent)
                                    .clickable {
                                        messageViewModel.onShareUserSelected(user)
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(RussianViolet),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val initials = buildString {
                                        user.firstName?.firstOrNull()?.let { append(it.uppercase()) }
                                        user.lastName?.firstOrNull()?.let { append(it.uppercase()) }
                                    }.ifEmpty { "U" }

                                    Text(
                                        text = initials,
                                        color = PureWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = listOfNotNull(user.firstName, user.lastName)
                                            .joinToString(" ")
                                            .ifEmpty { "User" },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RussianViolet
                                    )
                                    Text(
                                        text = user.userType,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        color = RussianViolet,
                        modifier = Modifier.clickable { onDismiss() }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    val sendModifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (sendEnabled) RussianViolet
                            else RussianViolet.copy(alpha = 0.4f)
                        )
                        .then(
                            if (sendEnabled) Modifier.clickable {
                                val selected = state.selectedUser
                                val postId = post.id
                                if (selected != null && postId != null) {
                                    messageViewModel.sendMessageWithPost(
                                        receiverId = selected.userId,
                                        sharedPostId = postId
                                    )
                                    onShared()
                                    onDismiss()
                                }
                            } else Modifier
                        )
                        .padding(horizontal = 28.dp, vertical = 10.dp)

                    Box(modifier = sendModifier) {
                        Text(
                            text = "Send",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
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

@Composable
fun RealPostCard(
    post: PostResponse,
    isLiked: Boolean,
    comments: List<CommentDto>,
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

            // Media display - CORRECTED VERSION
            if (post.mediaUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))

                // DEBUG LOG
                Log.d("POST_MEDIA", "Post ID: ${post.id}, Type: ${post.postType}, URL: ${post.mediaUrl}")

                when (post.postType) {
                    "image" -> {
                        AsyncImage(
                            model = post.mediaUrl,
                            contentDescription = "Post image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                Log.d("POST_MEDIA", "Loading image: ${post.mediaUrl}")
                            },
                            onSuccess = {
                                Log.d("POST_MEDIA", "Image loaded successfully: ${post.mediaUrl}")
                            },
                            onError = { error ->
                                Log.e("POST_MEDIA", "Failed to load image: ${post.mediaUrl}", error.result.throwable)
                            }
                        )
                    }
                    "video" -> {
                        // Use the actual video player
                        SimpleVideoPlayer(
                            videoUrl = post.mediaUrl,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Almond),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Unknown type: ${post.postType}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Comments
            if (comments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                comments.forEach { comment ->
                    Spacer(modifier = Modifier.height(8.dp))
                    CommentRow(comment = comment)
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
                    icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    label = "Like",
                    isActive = isLiked,
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
    icon: ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val tintColor = if (isActive) {
        RussianViolet
    } else {
        RussianViolet.copy(alpha = 0.4f)
    }
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
            tint = tintColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = tintColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CommentRow(comment: CommentDto) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        val initials = comment.authorName
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Almond),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.ifEmpty { "U" },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = comment.authorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RussianViolet
            )
            Text(
                text = comment.content,
                fontSize = 13.sp,
                color = RussianViolet
            )
        }
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val fixedTimestamp = if (!timestamp.endsWith("Z") && !timestamp.contains('+')) {
            "${timestamp}Z"
        } else {
            timestamp
        }
        val instant = Instant.parse(fixedTimestamp)
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