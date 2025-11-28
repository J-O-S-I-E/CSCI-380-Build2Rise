package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.build2rise.data.model.MessageResponse
import com.example.build2rise.ui.viewmodel.MessageViewModel
import com.example.build2rise.ui.viewmodel.ConversationDetailState
import com.example.build2rise.ui.viewmodel.SendMessageState
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.ProfileState
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    otherUserId: String,
    otherUserName: String,
    onBack: () -> Unit,
    viewModel: MessageViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    var currentUserId by remember { mutableStateOf("") }

    val conversationState by viewModel.conversationDetailState.collectAsState()
    val sendMessageState by viewModel.sendMessageState.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Get current user ID
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUserProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            currentUserId = (profileState as ProfileState.Success).profile.userId
        }
    }

    // Load conversation
    LaunchedEffect(otherUserId) {
        viewModel.getConversation(otherUserId)
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(conversationState) {
        if (conversationState is ConversationDetailState.Success) {
            val messages = (conversationState as ConversationDetailState.Success).conversation.messages
            if (messages.isNotEmpty()) {
                coroutineScope.launch {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
        }
    }

    // Reset send state and scroll after sending
    LaunchedEffect(sendMessageState) {
        if (sendMessageState is SendMessageState.Success) {
            messageText = ""
            viewModel.resetSendState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RussianViolet),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = otherUserName.split(" ")
                                .take(2)
                                .joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
                            Text(
                                initials,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            otherUserName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = RussianViolet
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite,
                    titleContentColor = RussianViolet
                )
            )
        },
        bottomBar = {
            MessageInputBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(otherUserId, messageText)
                    }
                },
                isLoading = sendMessageState is SendMessageState.Loading
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PureWhite)
                .padding(paddingValues)
        ) {
            when (val state = conversationState) {
                is ConversationDetailState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = RussianViolet)
                    }
                }
                is ConversationDetailState.Success -> {
                    if (state.conversation.messages.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No messages yet. Say hi! 👋",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.conversation.messages) { message ->
                                val isMe = message.senderId == currentUserId
                                MessageBubble(
                                    message = message,
                                    isMe = isMe,
                                    messageViewModel = viewModel
                                )
                            }
                        }
                    }
                }
                is ConversationDetailState.Error -> {
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
                                onClick = { viewModel.getConversation(otherUserId) },
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
}



@Composable
fun MessageInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Almond,
                unfocusedContainerColor = Almond,
                focusedBorderColor = Glaucous,
                unfocusedBorderColor = Color.LightGray
            ),
            maxLines = 4
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            enabled = messageText.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = RussianViolet
                )
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank()) RussianViolet else Color.Gray
                )
            }
        }
    }
}
@Composable
fun MessageBubble(
    message: MessageResponse,
    isMe: Boolean,
    messageViewModel: MessageViewModel
) {
    val sharedPosts by messageViewModel.sharedPosts.collectAsState()
    val sharedPostId = message.sharedPostId

    // If this message references a post, ensure we have it loaded
    LaunchedEffect(sharedPostId) {
        if (sharedPostId != null && !sharedPosts.containsKey(sharedPostId)) {
            messageViewModel.loadSharedPost(sharedPostId)
        }
    }

    val sharedPost = sharedPostId?.let { sharedPosts[it] }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        // Message bubble text
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isMe) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.content,
                color = if (isMe) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        // 🔥 If this message has a shared post, show the preview card below
        if (sharedPost != null) {
            Spacer(Modifier.height(4.dp))
            SharedPostPreviewCard(sharedPost)
        }
    }
}

