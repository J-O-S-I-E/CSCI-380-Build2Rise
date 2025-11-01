package com.example.build2rise.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

data class Post(val title: String, val type: String, val updated: String)

val samplePosts = listOf(
    Post("Title", "video", "updated today"),
    Post("Title", "image", "updated yesterday"),
    Post("Title", "video", "updated 2 days ago"),
    Post("Title", "image", "updated 3 days ago"),
    Post("Title", "video", "updated this week"),
    Post("Title", "image", "updated last week")
)

@Composable
fun InvestorProfilePosts() {
    var selectedBottomTab by remember { mutableStateOf("Feed") }
    var selectedProfileTab by remember { mutableStateOf("Posts") }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Almond)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BottomTab("Feed", Icons.Filled.Home, selectedBottomTab) { selectedBottomTab = it }
                BottomTab("AI Search", Icons.Filled.Search, selectedBottomTab) { selectedBottomTab = it }
                BottomTab("Messages", Icons.Filled.Email, selectedBottomTab) { selectedBottomTab = it }
                BottomTab("Profile", Icons.Filled.Person, selectedBottomTab) { selectedBottomTab = it }
            }
        }


    ) { padding ->
        if (selectedBottomTab == "Profile") {

            Column(
            modifier = Modifier
                .padding(padding)
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

            // Profile Info (Centered)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(RussianViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AI", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text("Angel Invest", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RussianViolet)
                Text("Investor", color = CaputMortuum, fontSize = 14.sp)
                Text("574 Connections", color = Glaucous, fontSize = 14.sp)
            }

            Spacer(Modifier.height(22.dp))

            // Tabs Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem("Posts", selectedProfileTab) { selectedProfileTab = "Posts" }
                TabItem("Supported Projects", selectedProfileTab) { selectedProfileTab = "Supported Projects" }
            }

            Spacer(Modifier.height(6.dp))
            Divider(color = Almond, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))

            // Tab Content
            if (selectedProfileTab == "Posts") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(samplePosts) { post -> PostCard(post) }
                }
            } else {
                InvestorProfileProjects()
            }
        }
        } else {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }
}
@Composable
fun TabItem(
    label: String,
    selected: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontWeight = if (selected == label) FontWeight.Bold else FontWeight.Normal,
        color = if (selected == label) RussianViolet else Color.Gray,
        modifier = Modifier.clickable { onClick() }
    )
}
@Composable
fun BottomTab(label: String, icon: ImageVector, selected: String, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(label) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected == label) RussianViolet else Color.Gray,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = if (selected == label) RussianViolet else Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
fun PostCard(post: Post) {
    Column(
        modifier = Modifier
            .background(Almond, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .height(160.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Almond, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(post.type, color = RussianViolet)
        }
        Spacer(Modifier.height(4.dp))
        Text(post.title, fontWeight = FontWeight.Bold, color = RussianViolet)
        Text(post.updated, color = Color.Gray, fontSize = 12.sp)
    }
}
