package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.data.model.ConversationResponse
import com.example.build2rise.ui.viewmodel.MessageViewModel
import com.example.build2rise.ui.viewmodel.ConversationsState
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import com.example.build2rise.data.model.PostResponse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp



@Composable
fun MessagesScreen(
    viewModel: MessageViewModel = viewModel(),
    onConversationClick: (String, String) -> Unit = { _, _ -> }
) {
    var searchQuery by remember { mutableStateOf("") }
    val conversationsState by viewModel.conversationsState.collectAsState()

    // Load conversations on screen open
    LaunchedEffect(Unit) {
        viewModel.getConversations()
    }

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
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = Almond, thickness = 1.dp)
        Spacer(Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search conversations", color = Color.Gray, fontSize = 14.sp) },
            trailingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Almond,
                unfocusedContainerColor = Almond,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(16.dp))

        // Conversations List
        when (val state = conversationsState) {
            is ConversationsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RussianViolet)
                }
            }
            is ConversationsState.Success -> {
                val filteredConversations = if (searchQuery.isBlank()) {
                    state.conversations
                } else {
                    state.conversations.filter { conversation ->
                        val name = "${conversation.firstName} ${conversation.lastName}"
                        name.contains(searchQuery, ignoreCase = true)
                    }
                }

                if (filteredConversations.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (searchQuery.isBlank()) "No messages yet" else "No results found",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredConversations) { conversation ->
                            ConversationCard(
                                conversation = conversation,
                                onClick = {
                                    val name = "${conversation.firstName} ${conversation.lastName}"
                                    onConversationClick(conversation.userId, name)
                                }
                            )
                        }
                    }
                }
            }
            is ConversationsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(state.message, color = Color.Red)
                        Button(
                            onClick = { viewModel.getConversations() },
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
}

@Composable
fun ConversationCard(
    conversation: ConversationResponse,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(RussianViolet),
            contentAlignment = Alignment.Center
        ) {
            val initials = "${conversation.firstName?.firstOrNull() ?: ""}${conversation.lastName?.firstOrNull() ?: ""}"
            Text(
                text = initials.ifEmpty { "U" },
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${conversation.firstName} ${conversation.lastName}",
                fontWeight = FontWeight.Bold,
                color = RussianViolet,
                fontSize = 16.sp
            )
            Text(
                conversation.lastMessage ?: "No messages yet",
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Spacer(Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                formatMessageTime(conversation.lastMessageTime),
                color = Color.Gray,
                fontSize = 12.sp
            )
            if (conversation.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(RussianViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        color = PureWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun formatMessageTime(timestamp: String?): String {
    if (timestamp == null) return ""
    return try {
        val instant = Instant.parse(timestamp)
        val now = Instant.now()
        val minutes = ChronoUnit.MINUTES.between(instant, now)

        when {
            minutes < 1 -> "now"
            minutes < 60 -> "${minutes}m"
            minutes < 1440 -> "${minutes / 60}h"
            minutes < 10080 -> "${minutes / 1440}d"
            else -> {
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd")
                    .withZone(ZoneId.systemDefault())
                formatter.format(instant)
            }
        }
    } catch (e: Exception) {
        ""
    }
}
@Composable
fun SharedPostPreviewCard(post: PostResponse) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Almond),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = "${post.firstName ?: ""} ${post.lastName ?: ""}".trim()
                    .ifEmpty { "User" },
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = RussianViolet
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = post.postDescription ?: "[No description]",
                fontSize = 13.sp,
                color = RussianViolet.copy(alpha = 0.8f),
                maxLines = 3
            )
        }
    }
}