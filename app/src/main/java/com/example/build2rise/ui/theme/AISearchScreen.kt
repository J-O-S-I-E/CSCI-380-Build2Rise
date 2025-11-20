package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.data.model.UserProfileResponse
import com.example.build2rise.ui.viewmodel.SearchViewModel
import com.example.build2rise.ui.viewmodel.SearchState
import com.example.build2rise.ui.viewmodel.ConnectionViewModel
import com.example.build2rise.ui.viewmodel.ConnectionActionState
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.ProfileState
import com.example.build2rise.ui.viewmodel.ProjectViewModel
import com.example.build2rise.ui.viewmodel.ProjectActionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISearchScreen(
    searchViewModel: SearchViewModel = viewModel(),
    connectionViewModel: ConnectionViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    projectViewModel: ProjectViewModel = viewModel()
) {
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var currentUserType by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchState by searchViewModel.searchState.collectAsState()
    val connectionActionState by connectionViewModel.connectionActionState.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()
    val projectActionState by projectViewModel.projectActionState.collectAsState()

    // Get current user type
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUserProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            currentUserType = (profileState as ProfileState.Success).profile.userType
        }
    }

    // Load all users on screen open
    LaunchedEffect(Unit) {
        searchViewModel.searchUsers()
    }

    // Show snackbar for connection actions
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(connectionActionState) {
        when (val state = connectionActionState) {
            is ConnectionActionState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                connectionViewModel.resetActionState()
                searchViewModel.searchUsers(userType = selectedUserType)
            }
            is ConnectionActionState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                connectionViewModel.resetActionState()
            }
            else -> {}
        }
    }

    // Show snackbar for project actions
    LaunchedEffect(projectActionState) {
        when (val state = projectActionState) {
            is ProjectActionState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                projectViewModel.resetActionState()
            }
            is ProjectActionState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                projectViewModel.resetActionState()
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
                .padding(16.dp)
        ) {
            // Header
            Text(
                "Discover",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet
            )

            Spacer(Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, startup, or industry...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = RussianViolet
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = RussianViolet
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { keyboardController?.hide() }
                ),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Almond,
                    unfocusedContainerColor = Almond,
                    focusedBorderColor = Glaucous,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(16.dp))

            // User Type Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedUserType == null,
                    onClick = {
                        selectedUserType = null
                        searchViewModel.searchUsers(userType = null)
                    },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
                FilterChip(
                    selected = selectedUserType == "founder",
                    onClick = {
                        selectedUserType = "founder"
                        searchViewModel.searchUsers(userType = "founder")
                    },
                    label = { Text("Founders") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
                FilterChip(
                    selected = selectedUserType == "investor",
                    onClick = {
                        selectedUserType = "investor"
                        searchViewModel.searchUsers(userType = "investor")
                    },
                    label = { Text("Investors") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Glaucous.copy(alpha = 0.2f),
                        selectedLabelColor = RussianViolet
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            // Search Results
            when (val state = searchState) {
                is SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = RussianViolet)
                    }
                }
                is SearchState.Success -> {
                    // Filter results based on search query
                    val filteredUsers = if (searchQuery.isBlank()) {
                        state.results.users
                    } else {
                        state.results.users.filter { user ->
                            val query = searchQuery.lowercase()

                            // Search in name
                            val fullName = "${user.firstName} ${user.lastName}".lowercase()
                            if (fullName.contains(query)) return@filter true

                            // Search in startup/firm name
                            val companyName = when (user.userType) {
                                "founder" -> user.profileData?.startupName?.lowercase()
                                "investor" -> user.profileData?.nameFirm?.lowercase()
                                else -> null
                            }
                            if (companyName?.contains(query) == true) return@filter true

                            // Search in industry
                            if (user.profileData?.industry?.lowercase()?.contains(query) == true) {
                                return@filter true
                            }

                            // Search in description (founders only)
                            if (user.profileData?.description?.lowercase()?.contains(query) == true) {
                                return@filter true
                            }

                            false
                        }
                    }

                    if (filteredUsers.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    if (searchQuery.isBlank()) "No users found" else "No results for \"$searchQuery\"",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                                if (searchQuery.isNotBlank()) {
                                    TextButton(onClick = { searchQuery = "" }) {
                                        Text("Clear search", color = RussianViolet)
                                    }
                                }
                            }
                        }
                    } else {
                        // Results count
                        Text(
                            text = "${filteredUsers.size} ${if (filteredUsers.size == 1) "user" else "users"} found",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(filteredUsers) { user ->
                                UserSearchCard(
                                    user = user,
                                    currentUserType = currentUserType,
                                    onConnect = {
                                        connectionViewModel.requestConnection(user.userId)
                                    },
                                    onSupportProject = {
                                        projectViewModel.supportProject(user.userId)
                                    },
                                    searchQuery = searchQuery
                                )
                            }
                        }
                    }
                }
                is SearchState.Error -> {
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
                                onClick = { searchViewModel.searchUsers() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RussianViolet
                                )
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
fun UserSearchCard(
    user: UserProfileResponse,
    currentUserType: String?,
    onConnect: () -> Unit,
    onSupportProject: () -> Unit,
    searchQuery: String = ""
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Almond),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(RussianViolet),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = "${user.firstName?.firstOrNull() ?: ""}${user.lastName?.firstOrNull() ?: ""}"
                    Text(
                        text = initials.ifEmpty { "U" },
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.width(16.dp))

                // User Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${user.firstName ?: ""} ${user.lastName ?: ""}".trim(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )

                    // Show startup name or firm name
                    val subtitle = if (user.userType == "founder") {
                        user.profileData?.startupName ?: "Founder"
                    } else {
                        user.profileData?.nameFirm ?: "Investor"
                    }
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = CaputMortuum,
                        fontWeight = FontWeight.Medium
                    )

                    // Show industry
                    user.profileData?.industry?.let { industry ->
                        Text(
                            text = industry,
                            fontSize = 12.sp,
                            color = Glaucous
                        )
                    }
                }
            }

            // Description (for founders)
            user.profileData?.description?.let { desc ->
                Spacer(Modifier.height(12.dp))
                Text(
                    text = desc,
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }

            // Location (for founders)
            if (user.userType == "founder") {
                user.profileData?.location?.let { location ->
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📍 $location",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        user.profileData.fundingStage?.let { stage ->
                            Text(
                                text = " • $stage",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Action Buttons - Show different buttons based on user types
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Connect Button (for everyone)
                Button(
                    onClick = onConnect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RussianViolet
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Connect", color = PureWhite, fontWeight = FontWeight.SemiBold)
                }

                // Support Project Button (only for investors viewing founders)
                if (currentUserType == "investor" && user.userType == "founder") {
                    Button(
                        onClick = onSupportProject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Glaucous
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Support", color = PureWhite, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}