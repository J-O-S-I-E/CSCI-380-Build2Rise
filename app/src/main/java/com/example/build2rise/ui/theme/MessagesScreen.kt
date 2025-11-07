package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

data class MessageItem(
    val sender: String,
    val message: String,
    val time: String,
    val unreadCount: Int = 0
)

val sampleMessages = listOf(
    MessageItem("MedHealth AI", "Would love to discuss pot...", "2m", unreadCount = 2),
    MessageItem("Marc Martinez", "Thanks for connecting! I’d lo..", "1h"),
    MessageItem("TechCorp", "Congratulations on your rec...", "3h"),
    MessageItem("Angel Investor", "I reviewed your proposal...", "1d"),
    MessageItem("Rise Fund", "Your pitch deck looks inte...", "2d")
)

@Composable
fun MessagesScreen() {
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
                "Messages",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = RussianViolet
            )
            Icon(
                Icons.Filled.Add,
                contentDescription = "New Message",
                tint = RussianViolet
            )
        }

        Spacer(Modifier.height(12.dp))
        Divider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(12.dp))

        // Search Bar
        SearchBar()

        Spacer(Modifier.height(16.dp))

        // Messages List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleMessages) { message ->
                MessageCard(message)
            }
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Almond, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Search conversations",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
    }
}

@Composable
fun MessageCard(message: MessageItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite, RoundedCornerShape(12.dp))
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular initials (like "CT" in Founder Profile)
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(RussianViolet),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.sender.split(" ")
                    .take(2)
                    .joinToString("") { it.first().uppercase() },
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(message.sender, fontWeight = FontWeight.Bold, color = RussianViolet)
            Text(message.message, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
        }

        Spacer(Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(message.time, color = Color.Gray, fontSize = 12.sp)
            if (message.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(RussianViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.unreadCount.toString(),
                        color = PureWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}