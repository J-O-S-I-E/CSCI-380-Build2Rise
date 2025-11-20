package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Card component for displaying a single investor post in grid
 * Used in MainScreen.kt Profile tab
 */
@Composable
fun PostCard(post: com.example.build2rise.data.model.PostResponse) {
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
            Text(
                text = post.postType,
                color = RussianViolet,
                fontSize = 14.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = post.postDescription?.take(20) ?: "Post",
            fontWeight = FontWeight.Bold,
            color = RussianViolet,
            fontSize = 12.sp,
            maxLines = 1
        )
        Text(
            text = formatPostTime(post.createdAt),  // Uses the function from FounderProfilePosts.kt
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

