package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.build2rise.data.model.ConnectionResponse
import com.example.build2rise.ui.viewmodel.ConnectionViewModel
import com.example.build2rise.ui.viewmodel.ConnectionsState
import com.example.build2rise.ui.viewmodel.RequestsState
import com.example.build2rise.ui.viewmodel.ConnectionActionState

@Composable
fun Connections(
    viewModel: ConnectionViewModel = viewModel(),
    onMessageClick: (String, String) -> Unit = { _, _ -> }
) {
    var selectedTab by remember { mutableStateOf(0) }
    var filterType by remember { mutableStateOf("All") }

    val connectionsState by viewModel.connectionsState.collectAsState()
    val requestsState by viewModel.requestsState.collectAsState()
    val actionState by viewModel.connectionActionState.collectAsState()

    // Load data on screen open
    LaunchedEffect(Unit) {
        viewModel.getConnections()
        viewModel.getPendingRequests()
    }

    // Show snackbar for actions
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(actionState) {
        when (val state = actionState) {
            is ConnectionActionState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetActionState()
            }
            is ConnectionActionState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetActionState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PureWhite)
                .padding(paddingValues)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Connections",
                    color = RussianViolet,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = PureWhite,
                contentColor = RussianViolet
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        val count = when (val state = connectionsState) {
                            is ConnectionsState.Success -> state.connections.totalCount
                            else -> 0
                        }
                        Text(
                            text = "My Connections ($count)",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        val count = when (val state = requestsState) {
                            is RequestsState.Success -> state.requests.totalCount
                            else -> 0
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Requests",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                            if (count > 0) {
                                Spacer(Modifier.width(4.dp))
                                Badge(
                                    containerColor = Glaucous,
                                    contentColor = PureWhite
                                ) {
                                    Text("$count", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                )
            }

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterType == "All",
                    onClick = { filterType = "All" },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
                FilterChip(
                    selected = filterType == "founder",
                    onClick = { filterType = "founder" },
                    label = { Text("Founders") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
                FilterChip(
                    selected = filterType == "investor",
                    onClick = { filterType = "investor" },
                    label = { Text("Investors") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
            }

            // Tab Content
            when (selectedTab) {
                0 -> MyConnectionsTab(connectionsState, filterType, onMessageClick)
                1 -> RequestsTab(requestsState, filterType, viewModel)
            }
        }
    }
}

@Composable
fun MyConnectionsTab(
    state: ConnectionsState,
    filterType: String,
    onMessage: (String, String) -> Unit
) {
    when (state) {
        is ConnectionsState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RussianViolet)
            }
        }
        is ConnectionsState.Success -> {
            val filteredConnections = state.connections.connections.filter { connection ->
                if (filterType == "All") true
                else connection.user1.userType == filterType || connection.user2.userType == filterType
            }

            if (filteredConnections.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No connections yet",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredConnections) { connection ->
                        ConnectionCard(connection = connection, onMessage = onMessage)
                    }
                }
            }
        }
        is ConnectionsState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(state.message, color = Color.Red)
                }
            }
        }
        else -> {}
    }
}

@Composable
fun RequestsTab(
    state: RequestsState,
    filterType: String,
    viewModel: ConnectionViewModel
) {
    when (state) {
        is RequestsState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RussianViolet)
            }
        }
        is RequestsState.Success -> {
            val filteredRequests = state.requests.connections.filter { connection ->
                if (filterType == "All") true
                else connection.user1.userType == filterType
            }

            if (filteredRequests.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No pending requests",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredRequests) { request ->
                        RequestCard(
                            request = request,
                            onAccept = { viewModel.acceptConnection(request.id) },
                            onReject = { viewModel.rejectConnection(request.id) }
                        )
                    }
                }
            }
        }
        is RequestsState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(state.message, color = Color.Red)
                }
            }
        }
        else -> {}
    }
}

@Composable
fun ConnectionCard(
    connection: ConnectionResponse,
    onMessage: (String, String) -> Unit
) {
    val displayUser = connection.user1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Almond),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    val initials = "${displayUser.firstName?.firstOrNull() ?: ""}${displayUser.lastName?.firstOrNull() ?: ""}"
                    Text(
                        text = initials.ifEmpty { "U" },
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Name and type
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${displayUser.firstName ?: ""} ${displayUser.lastName ?: ""}".trim(),
                        color = RussianViolet,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = displayUser.userType.replaceFirstChar { it.uppercase() },
                        color = CaputMortuum,
                        fontSize = 14.sp
                    )
                }

                // Options icon
                IconButton(onClick = { /* TODO: Show options */ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Options",
                        tint = RussianViolet
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Message Button
            Button(
                onClick = {
                    val name = "${displayUser.firstName ?: ""} ${displayUser.lastName ?: ""}".trim()
                    onMessage(displayUser.userId, name)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RussianViolet
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Message",
                    tint = PureWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Message", color = PureWhite, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun RequestCard(
    request: ConnectionResponse,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val sender = request.user1 // user1 is the sender

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Almond),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
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
                    val initials = "${sender.firstName?.firstOrNull() ?: ""}${sender.lastName?.firstOrNull() ?: ""}"
                    Text(
                        text = initials.ifEmpty { "U" },
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Name and type
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${sender.firstName ?: ""} ${sender.lastName ?: ""}".trim(),
                        color = RussianViolet,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sender.userType.replaceFirstChar { it.uppercase() },
                        color = CaputMortuum,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RussianViolet
                    )
                ) {
                    Text("Accept", color = PureWhite)
                }
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = RussianViolet
                    )
                ) {
                    Text("Decline")
                }
            }
        }
    }
}