//package com.example.build2rise.ui.theme
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Image
//import androidx.compose.material.icons.filled.VideoLibrary
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import coil.compose.AsyncImage
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PostCreationModal(
//    userInitials: String,
//    onDismiss: () -> Unit,
////    onPost: (description: String, mediaType: String?) -> Unit
//    onPost: (description: String, fileUri: Uri?) -> Unit
//) {
//    var description by remember { mutableStateOf("") }
//    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
//    var selectedMediaType by remember { mutableStateOf<String?>(null) }
//
//    val context = LocalContext.current
//
//    // Image picker
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            selectedFileUri = it
//            selectedMediaType = "image"
//        }
//    }
//
//    // Video picker
//    val videoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            selectedFileUri = it
//            selectedMediaType = "video"
//        }
//    }
//
//
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = PureWhite)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp)
//            ) {
//                // Header
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "Share an Update",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = RussianViolet
//                    )
//                    IconButton(onClick = onDismiss) {
//                        Icon(
//                            Icons.Default.Close,
//                            contentDescription = "Close",
//                            tint = RussianViolet
//                        )
//                    }
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                // User Avatar + Input
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.Top
//                ) {
//                    // Avatar
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(RussianViolet),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userInitials,
//                            color = PureWhite,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp
//                        )
//                    }
//
//                    Spacer(Modifier.width(12.dp))
//
//                    // Text Input
//                    OutlinedTextField(
//                        value = description,
//                        onValueChange = { description = it },
//                        placeholder = { Text("What's on your mind?") },
//                        modifier = Modifier.fillMaxWidth(),
//                        minLines = 4,
//                        maxLines = 8,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedContainerColor = Almond,
//                            unfocusedContainerColor = Almond,
//                            focusedBorderColor = Glaucous,
//                            unfocusedBorderColor = Color.LightGray
//                        )
//                    )
//                }
//                // Selected media preview
//                if (selectedFileUri != null) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(containerColor = Almond)
//                    ) {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            when (selectedMediaType) {
//                                "image" -> {
//                                    AsyncImage(
//                                        model = selectedFileUri,
//                                        contentDescription = "Selected image",
//                                        modifier = Modifier.fillMaxSize(),
//                                        contentScale = ContentScale.Crop
//                                    )
//                                }
//
//                                "video" -> {
//                                    // Video thumbnail placeholder
//                                    Box(
//                                        modifier = Modifier.fillMaxSize(),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Column(
//                                            horizontalAlignment = Alignment.CenterHorizontally
//                                        ) {
//                                            Icon(
//                                                Icons.Default.VideoLibrary,
//                                                contentDescription = "Video",
//                                                tint = RussianViolet,
//                                                modifier = Modifier.size(48.dp)
//                                            )
//                                            Spacer(Modifier.height(8.dp))
//                                            Text(
//                                                "Video selected",
//                                                color = RussianViolet,
//                                                fontSize = 14.sp
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//
//                            // Remove button
//                            IconButton(
//                                onClick = {
//                                    selectedFileUri = null
//                                    selectedMediaType = null
//                                },
//                                modifier = Modifier
//                                    .align(Alignment.TopEnd)
//                                    .padding(8.dp)
//                                    .background(
//                                        Color.Black.copy(alpha = 0.5f),
//                                        CircleShape
//                                    )
//                            ) {
//                                Icon(
//                                    Icons.Default.Close,
//                                    contentDescription = "Remove",
//                                    tint = Color.White,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(Modifier.height(16.dp))
//
//                    // Media Type Selection
//                    Text(
//                        "Add to your post:",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = RussianViolet
//                    )
//
//                    Spacer(Modifier.height(8.dp))
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        // Photo Option
//                        MediaTypeButton(
//                            icon = Icons.Default.Image,
//                            label = "Photo",
//                            selected = selectedMediaType == "image",
//                            onClick = {
//                                imagePickerLauncher.launch("image/*")
//                            }
//                        )
//
//                        // Video Option
//                        MediaTypeButton(
//                            icon = Icons.Default.VideoLibrary,
//                            label = "Video",
//                            selected = selectedMediaType == "video",
//                            onClick = {
//                                videoPickerLauncher.launch("image/*")
//                            }
//                        )
//                    }
//
//                    Spacer(Modifier.height(20.dp))
//
//                    // Post Button
//                    Button(
//                        onClick = {
//                            onPost(description, selectedFileUri)
//                            onDismiss()
//
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(48.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = RussianViolet,
//                            disabledContainerColor = RussianViolet.copy(alpha = 0.5f)
//                        ),
//                        enabled = description.isNotBlank() || selectedFileUri != null
//                    ) {
//                        Text(
//                            "Post",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = PureWhite
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun MediaTypeButton(
//        icon: androidx.compose.ui.graphics.vector.ImageVector,
//        label: String,
//        selected: Boolean,
//        onClick: () -> Unit
//    ) {
//        Card(
//            modifier = Modifier.clickable(onClick = onClick),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = if (selected) Glaucous.copy(alpha = 0.1f) else Almond
//            ),
//            border = androidx.compose.foundation.BorderStroke(
//                width = if (selected) 2.dp else 1.dp,
//                color = if (selected) Glaucous else Color.LightGray
//            )
//        ) {
//            Row(
//                modifier = Modifier.padding(12.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = label,
//                    tint = if (selected) Glaucous else RussianViolet,
//                    modifier = Modifier.size(20.dp)
//                )
//                Text(
//                    label,
//                    fontSize = 14.sp,
//                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
//                    color = if (selected) Glaucous else RussianViolet
//                )
//            }
//        }
//    }
//}

package com.example.build2rise.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationModal(
    userInitials: String,
    onDismiss: () -> Unit,
    onPost: (description: String, fileUri: Uri?) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedMediaType by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            selectedMediaType = "image"
        }
    }

    // Video picker
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            selectedMediaType = "video"
        }
    }

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

                // Selected media preview
                if (selectedFileUri != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Almond)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (selectedMediaType) {
                                "image" -> {
                                    AsyncImage(
                                        model = selectedFileUri,
                                        contentDescription = "Selected image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                "video" -> {
                                    // Video thumbnail placeholder
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                Icons.Default.VideoLibrary,
                                                contentDescription = "Video",
                                                tint = RussianViolet,
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Spacer(Modifier.height(8.dp))
                                            Text(
                                                "Video selected",
                                                color = RussianViolet,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Remove button
                            IconButton(
                                onClick = {
                                    selectedFileUri = null
                                    selectedMediaType = null
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

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
                        selected = selectedMediaType == "image",
                        onClick = {
                            imagePickerLauncher.launch("image/*")
                        }
                    )

                    // Video Option
                    MediaTypeButton(
                        icon = Icons.Default.VideoLibrary,
                        label = "Video",
                        selected = selectedMediaType == "video",
                        onClick = {
                            videoPickerLauncher.launch("video/*")  // ✅ FIXED: was "image/*"
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Post Button
                Button(
                    onClick = {
                        onPost(description, selectedFileUri)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RussianViolet,
                        disabledContainerColor = RussianViolet.copy(alpha = 0.5f)
                    ),
                    enabled = description.isNotBlank() || selectedFileUri != null
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