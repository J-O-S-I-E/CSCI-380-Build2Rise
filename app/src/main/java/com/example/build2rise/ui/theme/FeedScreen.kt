package com.example.build2rise.ui

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

// Data classes for feed posts
data class FeedPost(
    val id: String,
    val authorName: String,
    val authorInitials: String,
    val authorType: String, // "Founder" or "Investor"
    val industry: String,
    val timestamp: String,
    val content: String,
    val hasImage: Boolean = false,
    val hasVideo: Boolean = false,
    val likes: Int = 0,
    val comments: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Founders", "Investors")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Top Bar
        FeedTopBar()

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

        // Feed Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Share Update Section (only show on Founders tab)
            if (selectedTab == 0) {
                item {
                    ShareUpdateCard()
                }
            }

            // Posts
            items(
                if (selectedTab == 0) getFounderPosts() else getInvestorPosts()
            ) { post ->
                PostCard(post = post)
            }
        }
    }
}

@Composable
fun FeedTopBar() {
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
        Row {
            IconButton(onClick = { /* Add post action */ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Post",
                    tint = RussianViolet
                )
            }
            IconButton(onClick = { /* More options */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More Options",
                    tint = RussianViolet
                )
            }
        }
    }
}

@Composable
fun ShareUpdateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Almond),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile + Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RussianViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Input Field
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(PureWhite)
                        .clickable { /* Open post creator */ }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Share an update",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = RussianViolet,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PostTypeButton(
                    icon = Icons.Outlined.CameraAlt,
                    label = "Photo",
                    modifier = Modifier.weight(1f)
                )
                PostTypeButton(
                    icon = Icons.Outlined.Videocam,
                    label = "Video",
                    modifier = Modifier.weight(1f)
                )
                PostTypeButton(
                    icon = Icons.Outlined.Article,
                    label = "Pitch",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PostTypeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /* Handle post type selection */ },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PureWhite,
            contentColor = RussianViolet
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, RussianViolet.copy(alpha = 0.3f)),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PostCard(post: FeedPost) {
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
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(RussianViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorInitials,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Author Info
                    Column {
                        Text(
                            text = post.authorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RussianViolet
                        )
                        Text(
                            text = "${post.authorType}, ${post.industry}, ${post.timestamp}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Action Icon (Link/Message)
                IconButton(onClick = { /* Handle connection */ }) {
                    Icon(
                        imageVector = if (post.authorType == "Founder") Icons.Outlined.Link else Icons.Outlined.Message,
                        contentDescription = "Connect",
                        tint = RussianViolet,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Content
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = RussianViolet.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )

            // Image placeholder if post has image
            if (post.hasImage) {
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
                        text = "image",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // Engagement Actions
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Almond, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EngagementButton(
                    icon = Icons.Outlined.ThumbUp,
                    label = if (post.likes > 0) "${post.likes}" else "Like"
                )
                EngagementButton(
                    icon = Icons.Outlined.Comment,
                    label = if (post.comments > 0) "${post.comments}" else "Comment"
                )
                EngagementButton(
                    icon = Icons.Outlined.Share,
                    label = "Share"
                )
            }
        }
    }
}

@Composable
fun EngagementButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { /* Handle engagement */ }
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

// Sample Data Functions
fun getFounderPosts(): List<FeedPost> {
    return listOf(
        FeedPost(
            id = "1",
            authorName = "TechCorp",
            authorInitials = "TC",
            authorType = "Founder",
            industry = "Fintech",
            timestamp = "2h ago",
            content = "Just closed our pre-seed round! 🎉 Grateful to our early investors who believed in our vision to democratize financial services. Next stop: building the future of banking.",
            likes = 24,
            comments = 8
        ),
        FeedPost(
            id = "2",
            authorName = "Sarah Chen",
            authorInitials = "SC",
            authorType = "Founder",
            industry = "HealthTech",
            timestamp = "5h ago",
            content = "Excited to share that we've reached 10,000 users on our mental health platform! Thank you to everyone who's supported us on this journey. We're just getting started.",
            hasImage = false,
            likes = 56,
            comments = 15
        ),
        FeedPost(
            id = "3",
            authorName = "GreenTech Solutions",
            authorInitials = "GS",
            authorType = "Founder",
            industry = "CleanTech",
            timestamp = "1d ago",
            content = "Our new carbon capture prototype is ready for testing! Looking for partners and investors interested in sustainability. DM me if you want to learn more about our technology.",
            hasImage = true,
            likes = 89,
            comments = 23
        )
    )
}

fun getInvestorPosts(): List<FeedPost> {
    return listOf(
        FeedPost(
            id = "4",
            authorName = "Angel Investor",
            authorInitials = "AI",
            authorType = "Investor",
            industry = "Multiple",
            timestamp = "4h ago",
            content = "Looking for B2B SaaS startups with \$10K+ MRR for \$50K-\$250K seed investments. Strong focus on product-market fit and defensible moats. DM me your deck!",
            hasImage = true,
            likes = 45,
            comments = 12
        ),
        FeedPost(
            id = "5",
            authorName = "VentureCapital Fund",
            authorInitials = "VC",
            authorType = "Investor",
            industry = "EdTech",
            timestamp = "6h ago",
            content = "Our fund is actively looking for EdTech startups focused on K-12 education. Ticket size: \$500K-\$2M for Series A. Must have proven traction and clear path to profitability.",
            likes = 67,
            comments = 18
        ),
        FeedPost(
            id = "6",
            authorName = "Impact Investor",
            authorInitials = "II",
            authorType = "Investor",
            industry = "Social Impact",
            timestamp = "1d ago",
            content = "Passionate about funding startups that create positive social impact. Currently interested in affordable housing tech, food security, and climate solutions. Let's connect!",
            hasImage = false,
            likes = 92,
            comments = 31
        )
    )
}