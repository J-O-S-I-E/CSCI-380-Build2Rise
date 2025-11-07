package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FounderProfileProjects() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(RussianViolet),
                contentAlignment = Alignment.Center
            ) {
                Text("GS", color = PureWhite, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text("GreenStart", fontWeight = FontWeight.Bold, color = RussianViolet)
                Text("Founder, CleanTech", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Almond, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("video", color = RussianViolet)
        }

        Spacer(Modifier.height(8.dp))
        Text("Product description", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.ThumbUp, contentDescription = "Like", tint = RussianViolet)
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Filled.ChatBubbleOutline, contentDescription = "Comment", tint = RussianViolet)
        }
    }
}
