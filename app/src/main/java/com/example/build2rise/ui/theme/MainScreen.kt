package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.ProfileState
import com.example.build2rise.ui.viewmodel.PostViewModel
import com.example.build2rise.ui.viewmodel.PostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

/**
 * Main container for the app with bottom navigation
 * This handles navigation between all main screens
 */
@Composable
fun MainScreen(userType: String = "founder") {
    var selectedTab by remember { mutableStateOf("Feed") }
    var showConnectionsScreen by remember { mutableStateOf(false) }
    var showChatFromConnections by remember { mutableStateOf(false) }
    var chatUserId by remember { mutableStateOf("") }
    var chatUserName by remember { mutableStateOf("") }

    var showOtherUserProfile by remember { mutableStateOf(false) }
    var viewingUserId by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            // Only show bottom nav if not viewing connections
            if (!showConnectionsScreen && !showChatFromConnections && !showOtherUserProfile) {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        },
        containerColor = PureWhite
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // ADD THIS: Show other user's profile
                showOtherUserProfile -> {
                    OtherUserProfileScreen(
                        userId = viewingUserId,
                        onBack = { showOtherUserProfile = false },
                        onMessageClick = { userId, userName ->
                            chatUserId = userId
                            chatUserName = userName
                            showOtherUserProfile = false
                            showChatFromConnections = true
                        }
                    )
                }

                // Show chat from connections
                showChatFromConnections -> {
                    ChatScreen(
                        otherUserId = chatUserId,
                        otherUserName = chatUserName,
                        onBack = {
                            showChatFromConnections = false
                            showConnectionsScreen = true  // Go back to connections
                        }
                    )
                }
                // Show connections screen
                showConnectionsScreen -> {
                    ConnectionsScreenWrapper(
                        onBack = { showConnectionsScreen = false },
                        onMessageClick = { userId, userName ->
                            chatUserId = userId
                            chatUserName = userName
                            showConnectionsScreen = false
                            showChatFromConnections = true
                        }
                    )
                }

                else -> {
                    // Switch between screens based on selected tab
                    when (selectedTab) {
                        "Feed" -> FeedScreen(
                            onProfileClick = { userId ->
                                viewingUserId = userId
                                showOtherUserProfile = true
                            }
                        )
                        "Search" -> DiscoverScreen(
                            onProfileClick = { userId ->
                                viewingUserId = userId
                                showOtherUserProfile = true
                            }
                        )
//                    "Messages" -> MessagesScreen()
                        "Messages" -> {
                            var showChatScreen by remember { mutableStateOf(false) }
                            var selectedUserId by remember { mutableStateOf("") }
                            var selectedUserName by remember { mutableStateOf("") }

                            if (showChatScreen) {
                                ChatScreen(
                                    otherUserId = selectedUserId,
                                    otherUserName = selectedUserName,
                                    onBack = {
                                        showChatScreen = false
                                    }
                                )
                            } else {
                                MessagesScreen(
                                    onConversationClick = { userId, userName ->
                                        selectedUserId = userId
                                        selectedUserName = userName
                                        showChatScreen = true
                                    }
                                )
                            }
                        }

                        "Profile" -> ProfileScreenWrapper(
                            userType = userType,
                            onNavigateToConnections = { showConnectionsScreen = true }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reusable Bottom Navigation Bar Component
 */
@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Almond)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            label = "Feed",
            icon = Icons.Filled.Home,
            selected = selectedTab,
            onClick = onTabSelected
        )
        BottomNavItem(
            label = "Search",
            icon = Icons.Filled.Search,
            selected = selectedTab,
            onClick = onTabSelected
        )
        BottomNavItem(
            label = "Messages",
            icon = Icons.Filled.Email,
            selected = selectedTab,
            onClick = onTabSelected
        )
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            selected = selectedTab,
            onClick = onTabSelected
        )
    }
}

/**
 * Individual Bottom Navigation Item
 */
@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    selected: String,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(label) }
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected == label) RussianViolet else CaputMortuum.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (selected == label) RussianViolet else CaputMortuum.copy(alpha = 0.5f),
            fontSize = 11.sp,
            fontWeight = if (selected == label) FontWeight.Medium else FontWeight.Normal
        )
    }
}

/**
 * Connections Screen Wrapper with back navigation
 */
@Composable
fun ConnectionsScreenWrapper(
    onBack: () -> Unit,
    onMessageClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Back button header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RussianViolet
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "My Connections",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet
            )
        }

        // Connections content
        Connections(
            onMessageClick = onMessageClick
        )
    }
}

/**
 * Profile screen wrapper that fetches data and passes to content screens
 */
@Composable
fun ProfileScreenWrapper(
    userType: String,
    onNavigateToConnections: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileState by profileViewModel.profileState.collectAsState()

    // Load profile on screen open
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUserProfile()
    }

    when (val state = profileState) {
        is ProfileState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RussianViolet)
            }
        }

        is ProfileState.Success -> {
            val profile = state.profile
            if (userType == "founder") {
                FounderProfileContent(
                    profile = profile,
                    onNavigateToConnections = onNavigateToConnections
                )
            } else {
                InvestorProfileContent(
                    profile = profile,
                    onNavigateToConnections = onNavigateToConnections
                )
            }
        }

        is ProfileState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, color = Color.Red)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { profileViewModel.getCurrentUserProfile() },
                        colors = ButtonDefaults.buttonColors(containerColor = RussianViolet)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }

        else -> {}
    }
}

/**
 * Founder Profile Content with Real Data
 */
@Composable
fun FounderProfileContent(
    profile: com.example.build2rise.data.model.UserProfileResponse,
    onNavigateToConnections: () -> Unit = {},
    postViewModel: PostViewModel = viewModel()
) {
    var selectedProfileTab by remember { mutableStateOf("Posts") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = RussianViolet
            )

            // Logout button
            IconButton(
                onClick = {
                    // Clear session
                    val prefs = context.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
                    prefs.edit {
                        clear()
                    }

                    // Restart activity
                    (context as? android.app.Activity)?.let { activity ->
                        activity.finish()
                        activity.startActivity(activity.intent)
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = RussianViolet
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Profile Info - Using Real Data
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(RussianViolet, androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Show initials from real data
                val initials = "${profile.firstName?.firstOrNull() ?: ""}${profile.lastName?.firstOrNull() ?: ""}"
                Text(
                    initials.ifEmpty { "U" },
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            }
            Spacer(Modifier.height(8.dp))

            // Show startup name from profile data
            Text(
                profile.profileData?.startupName ?: "Founder",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RussianViolet
            )
            Text("Founder", color = CaputMortuum, fontSize = 14.sp)

            // Connections count - placeholder for now
            Text(
                text = "Connections",
                color = Glaucous,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToConnections() }
            )

            // Display About/Bio section using startup description
            if (!profile.profileData?.description.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Almond.copy(alpha = 0.3f)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = profile.profileData?.description ?: "",
                            fontSize = 14.sp,
                            color = RussianViolet.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileTab("Posts", selectedProfileTab) { selectedProfileTab = "Posts" }
            ProfileTab("Projects", selectedProfileTab) { selectedProfileTab = "Projects" }
        }

        Spacer(Modifier.height(6.dp))
        HorizontalDivider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(10.dp))

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            if (selectedProfileTab == "Posts") {
                // Load user's posts
                val postState by postViewModel.postState.collectAsState()

                LaunchedEffect(Unit) {
                    postViewModel.getAllPosts()
                }

                when (val state = postState) {
                    is PostState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = RussianViolet)
                        }
                    }
                    is PostState.Success -> {
                        // Filter to show only current user's posts
                        val userPosts = state.feed.posts.filter { post ->
                            post.userId == profile.userId
                        }

                        if (userPosts.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No posts yet",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 80.dp)
                            ) {
                                items(userPosts) { post -> FounderPostCard(post) }
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
                                    onClick = { postViewModel.getAllPosts() },
                                    colors = ButtonDefaults.buttonColors(containerColor = RussianViolet)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    else -> {}
                }
            } else {
                FounderProfileProjects(profile=profile)
            }
        }
    }
}

/**
 * Investor Profile Content with Real Data
 */
@Composable
fun InvestorProfileContent(
    profile: com.example.build2rise.data.model.UserProfileResponse,
    onNavigateToConnections: () -> Unit = {},
    postViewModel: PostViewModel = viewModel()
) {
    var selectedProfileTab by remember { mutableStateOf("Posts") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(16.dp)
    ) {
        // Header Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profile", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = RussianViolet)

            // Logout button
            IconButton(
                onClick = {
                    // Clear session
                    val prefs = context.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
                    prefs.edit {
                        clear()
                    }

                    // Restart activity
                    (context as? android.app.Activity)?.let { activity ->
                        activity.finish()
                        activity.startActivity(activity.intent)
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = RussianViolet
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Profile Info - Using Real Data
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(RussianViolet, androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Show initials from real data
                val initials = "${profile.firstName?.firstOrNull() ?: ""}${profile.lastName?.firstOrNull() ?: ""}"
                Text(
                    initials.ifEmpty { "U" },
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            }
            Spacer(Modifier.height(8.dp))

            // Show firm name from profile data
            Text(
                profile.profileData?.nameFirm ?: "Investor",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RussianViolet
            )
            Text("Investor", color = CaputMortuum, fontSize = 14.sp)

            // Connections count - placeholder for now
            Text(
                text = "Connections",
                color = Glaucous,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToConnections() }
            )
            // Display About/Bio section for investors
            val investorBio = buildString {

                val details = mutableListOf<String>()
                profile.profileData?.industry?.let { details.add("Industry: $it") }
                profile.profileData?.fundingStagePreference?.let { details.add("Focus: $it") }
                profile.profileData?.investmentRange?.let { details.add("Investment Range: $it") }

                if (details.isNotEmpty()) {
                    append(details.joinToString("\n"))
                }
            }.trim()

            if (investorBio.isNotBlank()) {
                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Almond.copy(alpha = 0.3f)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = investorBio,
                            fontSize = 14.sp,
                            color = RussianViolet.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // Tabs Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileTab("Posts", selectedProfileTab) { selectedProfileTab = "Posts" }
            ProfileTab("Supported Projects", selectedProfileTab) {
                selectedProfileTab = "Supported Projects"
            }
        }

        Spacer(Modifier.height(6.dp))
        HorizontalDivider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(10.dp))

        Box(modifier = Modifier.weight(1f)) {
            // Tab Content
            if (selectedProfileTab == "Posts") {
                // Load user's posts
                val postState by postViewModel.postState.collectAsState()

                LaunchedEffect(Unit) {
                    postViewModel.getAllPosts()
                }

                when (val state = postState) {
                    is PostState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = RussianViolet)
                        }
                    }
                    is PostState.Success -> {
                        // Filter to show only current user's posts
                        val userPosts = state.feed.posts.filter { post ->
                            post.userId == profile.userId
                        }

                        if (userPosts.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No posts yet",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 80.dp)
                            ) {
                                items(userPosts) { post -> PostCard(post) }
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
                                    onClick = { postViewModel.getAllPosts() },
                                    colors = ButtonDefaults.buttonColors(containerColor = RussianViolet)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    else -> {}
                }
            } else {
                InvestorProfileProjects()
            }
        }
    }
}

/**
 * Reusable Profile Tab Component
 */
@Composable
fun ProfileTab(
    label: String,
    selected: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontWeight = if (selected == label) FontWeight.Bold else FontWeight.Normal,
        color = if (selected == label) RussianViolet else CaputMortuum.copy(alpha = 0.6f),
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    )
}

