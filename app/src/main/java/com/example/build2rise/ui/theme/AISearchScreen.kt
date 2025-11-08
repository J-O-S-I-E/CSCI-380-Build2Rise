package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AIProfile(
    val initials: String,
    val name: String,
    val role: String,
    val description: String,
    val location: String,
    val stage: String,
    val tag: String
)

val sampleInvestors = listOf(
    AIProfile(
        initials = "TC",
        name = "MedHealth AI",
        role = "Founder, HealthTech",
        description = "Building AI-powered diagnostic tools for rural healthcare. 50K+ patients served, seeking strategic partners and growth capital.",
        location = "Austin",
        stage = "Series A",
        tag = "HealthTech"
    ),
    AIProfile(
        initials = "MM",
        name = "Marc Martinez",
        role = "Angel Investor, Ex-Google",
        description = "Focus on early-stage B2B SaaS. 15+ investments including 3 exits. Looking for technical founders solving enterprise problems.",
        location = "San Francisco",
        stage = "Series A",
        tag = "B2B SaaS"
    )
)

val sampleFounders = listOf(
    AIProfile(
        initials = "AC",
        name = "Alicia Chen",
        role = "Founder, FinTech",
        description = "Developing mobile-first finance management apps for Gen Z. Seeking seed funding and product mentors.",
        location = "New York",
        stage = "Seed",
        tag = "FinTech"
    ),
    AIProfile(
        initials = "RB",
        name = "Ravi Bhatt",
        role = "Co-founder, Clean Energy",
        description = "Working on affordable solar microgrids for rural communities. Looking for impact investors and collaborators.",
        location = "Denver",
        stage = "Pre-Series A",
        tag = "CleanTech"
    )
)

@Composable
fun AISearchScreen() {
    var selectedTab by remember { mutableStateOf("Investors") }
    var searchQuery by remember { mutableStateOf("") }

    val profiles = if (selectedTab == "Investors") sampleInvestors else sampleFounders

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Build2Rise",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = RussianViolet
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = RussianViolet)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = RussianViolet)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabItem("Investors", selectedTab == "Investors") { selectedTab = "Investors" }
            TabItem("Founders", selectedTab == "Founders") { selectedTab = "Founders" }
        }

        Spacer(Modifier.height(10.dp))

        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Almond, RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${profiles.size} results based on your search",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(12.dp))

        // Results List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(profiles) { profile ->
                ProfileCard(profile)
            }
        }
    }
}


@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) RussianViolet else Color.Gray,
            fontSize = 15.sp
        )
        if (isSelected) {
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .width(50.dp)
                    .height(2.dp)
                    .background(RussianViolet)
            )
        }
    }
}

@Composable
fun ProfileCard(profile: AIProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDFDFD), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Initials Circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(RussianViolet),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.initials,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name, fontWeight = FontWeight.Bold, color = RussianViolet)
                Text(profile.role, fontSize = 13.sp, color = Color.Gray)
            }

            // Connect Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD7E3FF)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("Connect", color = Color(0xFF2D3E7D), fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(profile.description, fontSize = 13.sp, color = Color.Gray)

        Spacer(Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📍 ${profile.location}", fontSize = 12.sp, color = Color.Gray)
            Text("💰 ${profile.stage}", fontSize = 12.sp, color = Color.Gray)
            Text("🏷️ ${profile.tag}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}