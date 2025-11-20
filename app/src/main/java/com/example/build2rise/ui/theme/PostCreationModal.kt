package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationModal(
    userInitials: String,
    onDismiss: () -> Unit,
    onPost: (description: String, mediaType: String?) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedMediaType by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Share an Update",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = RussianViolet
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // User Avatar + Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(RussianViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userInitials,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Text Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("What's on your mind?") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Almond,
                            unfocusedContainerColor = Almond,
                            focusedBorderColor = Glaucous,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Media Type Selection
                Text(
                    "Add to your post:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = RussianViolet
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Photo Option
                    MediaTypeButton(
                        icon = Icons.Default.Image,
                        label = "Photo",
                        selected = selectedMediaType == "photo",
                        onClick = {
                            selectedMediaType = if (selectedMediaType == "photo") null else "photo"
                        }
                    )

                    // Video Option
                    MediaTypeButton(
                        icon = Icons.Default.VideoLibrary,
                        label = "Video",
                        selected = selectedMediaType == "video",
                        onClick = {
                            selectedMediaType = if (selectedMediaType == "video") null else "video"
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Post Button
                Button(
                    onClick = {
                        if (description.isNotBlank()) {
                            onPost(description, selectedMediaType)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RussianViolet,
                        disabledContainerColor = RussianViolet.copy(alpha = 0.5f)
                    ),
                    enabled = description.isNotBlank()
                ) {
                    Text(
                        "Post",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PureWhite
                    )
                }
            }
        }
    }
}

@Composable
fun MediaTypeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Glaucous.copy(alpha = 0.1f) else Almond
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) Glaucous else Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) Glaucous else RussianViolet,
                modifier = Modifier.size(20.dp)
            )
            Text(
                label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Glaucous else RussianViolet
            )
        }
    }
}