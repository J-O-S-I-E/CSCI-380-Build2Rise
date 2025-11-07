package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

/**
 * Main container for the app with bottom navigation
 * This handles navigation between all main screens
 */
@Composable
fun MainScreen(userType: String = "founder") {
    var selectedTab by remember { mutableStateOf("Feed") }
    var showConnectionsScreen by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            // Only show bottom nav if not viewing connections
            if (!showConnectionsScreen) {
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
            // Show connections screen or main navigation
            if (showConnectionsScreen) {
                ConnectionsScreenWrapper(
                    onBack = { showConnectionsScreen = false }
                )
            } else {
                // Switch between screens based on selected tab
                when (selectedTab) {
                    "Feed" -> FeedScreen()
                    "AI Search" -> AISearchScreen()
                    "Messages" -> MessagesScreen()
                    "Profile" -> ProfileScreen(
                        userType = userType,
                        onNavigateToConnections = { showConnectionsScreen = true }
                    )
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
            label = "AI Search",
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
 * Placeholder screens - Replace with actual screen implementations
 */
@Composable
fun AISearchScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AI Search Screen",
            fontSize = 20.sp,
            color = RussianViolet,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Connections Screen Wrapper with back navigation
 */
@Composable
fun ConnectionsScreenWrapper(onBack: () -> Unit) {
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
                    imageVector = Icons.Default.ArrowBack,
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
        Connections()
    }
}

/**
 * Profile screen that shows either Founder or Investor profile
 * based on user type
 */
@Composable
fun ProfileScreen(
    userType: String,
    onNavigateToConnections: () -> Unit = {}
) {
    if (userType == "founder") {
        FounderProfileContent(onNavigateToConnections)
    } else {
        InvestorProfileContent(onNavigateToConnections)
    }
}

/**
 * Founder Profile Content
 * Uses FounderProfilePosts data and components
 */
@Composable
fun FounderProfileContent(onNavigateToConnections: () -> Unit = {}) {
    var selectedProfileTab by remember { mutableStateOf("Posts") }

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
            Icon(Icons.Filled.Person, contentDescription = "Settings", tint = RussianViolet)
        }

        Spacer(Modifier.height(20.dp))

        // Profile Info
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
                Text("CT", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "CleanTech",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RussianViolet
            )
            Text("Founder", color = CaputMortuum, fontSize = 14.sp)
            Text(
                text = "574 Connections",
                color = Glaucous,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToConnections() }
            )
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
        Divider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(10.dp))

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            if (selectedProfileTab == "Posts") {
                // Show founder posts grid
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(founderPosts) { post -> FounderPostCard(post) }
                }
            } else {
                FounderProfileProjects()
            }
        }
    }
}

/**
 * Investor Profile Content
 * Uses InvestorProfilePosts data and components
 */
@Composable
fun InvestorProfileContent(onNavigateToConnections: () -> Unit = {}) {
    var selectedProfileTab by remember { mutableStateOf("Posts") }

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
            Icon(Icons.Filled.Person, contentDescription = "Settings", tint = RussianViolet)
        }

        Spacer(Modifier.height(20.dp))

        // Profile Info
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
                Text("AI", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Angel Invest",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RussianViolet
            )
            Text("Investor", color = CaputMortuum, fontSize = 14.sp)
            Text(
                text = "574 Connections",
                color = Glaucous,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToConnections() }
            )
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
        Divider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(10.dp))

        Box(modifier = Modifier.weight(1f)) {
            // Tab Content
            if (selectedProfileTab == "Posts") {
                // Show investor posts grid
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(samplePosts) { post -> PostCard(post) }
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