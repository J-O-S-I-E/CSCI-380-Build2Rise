package com.example.build2rise.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.data.model.FounderMatchDTO
import com.example.build2rise.data.model.InvestorMatchDTO
import com.example.build2rise.ui.viewmodel.*
import kotlinx.coroutines.launch

enum class DiscoverTab {
    SMART_MATCHES,
    SEARCH
}

@Composable
fun DiscoverScreen(
    onProfileClick: (String) -> Unit = {},
    discoverViewModel: DiscoverViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    connectionViewModel: ConnectionViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    projectViewModel: ProjectViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(DiscoverTab.SMART_MATCHES) }
    var currentUserType by remember { mutableStateOf("") }

    val profileState by profileViewModel.profileState.collectAsState()

    // Get current user info
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUserProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val profile = (profileState as ProfileState.Success).profile
            currentUserType = profile.userType
        }
    }

    // Load matches when user info is available
    LaunchedEffect(currentUserType, selectedTab) {
        if (currentUserType.isNotEmpty() && selectedTab == DiscoverTab.SMART_MATCHES) {
            discoverViewModel.loadMatches(currentUserType)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Surface(
            color = PureWhite,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = PureWhite
                ) {
                    Tab(
                        selected = selectedTab == DiscoverTab.SMART_MATCHES,
                        onClick = { selectedTab = DiscoverTab.SMART_MATCHES },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (selectedTab == DiscoverTab.SMART_MATCHES) RussianViolet else CaputMortuum.copy(alpha = 0.6f)
                                )
                                Text("Smart Match")
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == DiscoverTab.SEARCH,
                        onClick = { selectedTab = DiscoverTab.SEARCH },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (selectedTab == DiscoverTab.SEARCH) RussianViolet else CaputMortuum.copy(alpha = 0.6f)
                                )
                                Text("Search")
                            }
                        }
                    )
                }
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            DiscoverTab.SMART_MATCHES -> {
                SmartMatchesContent(
                    discoverViewModel = discoverViewModel,
                    connectionViewModel = connectionViewModel,
                    projectViewModel = projectViewModel,
                    currentUserType = currentUserType,
                    onProfileClick = onProfileClick
                )
            }
            DiscoverTab.SEARCH -> {
                AISearchScreenContent(
                    searchViewModel = searchViewModel,
                    connectionViewModel = connectionViewModel,
                    profileViewModel = profileViewModel,
                    projectViewModel = projectViewModel,
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}

@Composable
fun SmartMatchesContent(
    discoverViewModel: DiscoverViewModel,
    connectionViewModel: ConnectionViewModel,
    projectViewModel: ProjectViewModel,
    currentUserType: String,
    onProfileClick: (String) -> Unit
) {
    val uiState by discoverViewModel.uiState.collectAsState()
    val projectActionState by projectViewModel.projectActionState.collectAsState()
    val connectionActionState by connectionViewModel.connectionActionState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Show snackbar when project action completes
    LaunchedEffect(projectActionState) {
        when (val state = projectActionState) {
            is ProjectActionState.Success -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                projectViewModel.resetActionState()
            }
            is ProjectActionState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                projectViewModel.resetActionState()
            }
            else -> {}
        }
    }

    // Show snackbar when connection action completes
    LaunchedEffect(connectionActionState) {
        when (val state = connectionActionState) {
            is ConnectionActionState.Success -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                connectionViewModel.resetActionState()
            }
            is ConnectionActionState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                connectionViewModel.resetActionState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DiscoverUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(color = RussianViolet)
                        Text(
                            "Finding your best matches...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CaputMortuum
                        )
                    }
                }
            }

            is DiscoverUiState.FounderMatches -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        MatchStatisticsCard(
                            totalMatches = state.totalMatches,
                            averageScore = state.averageScore,
                            onRefresh = { discoverViewModel.loadMatches(currentUserType) }
                        )
                    }

                    if (state.matches.isEmpty()) {
                        item {
                            EmptyMatchesCard(currentUserType)
                        }
                    } else {
                        item {
                            Text(
                                text = "Top Matches for You",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RussianViolet,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(state.matches) { match ->
                            FounderMatchCard(
                                match = match,
                                onClick = { onProfileClick(match.founderUserId) },
                                onSupportClick = { userId ->
                                    // Actually support the project
                                    projectViewModel.supportProject(userId)
                                }
                            )
                        }
                    }
                }
            }

            is DiscoverUiState.InvestorMatches -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        MatchStatisticsCard(
                            totalMatches = state.totalMatches,
                            averageScore = state.averageScore,
                            onRefresh = { discoverViewModel.loadMatches(currentUserType) }
                        )
                    }

                    if (state.matches.isEmpty()) {
                        item {
                            EmptyMatchesCard(currentUserType)
                        }
                    } else {
                        item {
                            Text(
                                text = "Top Matches for You",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RussianViolet,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(state.matches) { match ->
                            InvestorMatchCard(
                                match = match,
                                onClick = { onProfileClick(match.investorUserId) },
                                onConnectClick = { userId ->
                                    // Actually send connection request
                                    connectionViewModel.requestConnection(userId)
                                }
                            )
                        }
                    }
                }
            }

            is DiscoverUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = CaputMortuum
                        )
                        Text(
                            text = state.message,
                            color = CaputMortuum,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = { discoverViewModel.loadMatches(currentUserType) },
                            colors = ButtonDefaults.buttonColors(containerColor = RussianViolet)
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }
        }

        // Snackbar at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun MatchStatisticsCard(
    totalMatches: Int,
    averageScore: Double,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Glaucous.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = RussianViolet
                        )
                        Text(
                            "Total Matches",
                            style = MaterialTheme.typography.labelMedium,
                            color = RussianViolet
                        )
                    }
                    Text(
                        "$totalMatches",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = RussianViolet
                        )
                        Text(
                            "Avg Score",
                            style = MaterialTheme.typography.labelMedium,
                            color = RussianViolet
                        )
                    }
                    Text(
                        "${averageScore.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                }
            }

            IconButton(onClick = onRefresh) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Refresh matches",
                    tint = RussianViolet
                )
            }
        }
    }
}

@Composable
fun EmptyMatchesCard(userType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Almond)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = CaputMortuum
            )
            Text(
                text = "No matches found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RussianViolet
            )
            Text(
                text = if (userType == "investor") {
                    "Complete your investor profile with preferences to get better matches with founders"
                } else {
                    "Complete your startup profile to get matched with relevant investors"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = CaputMortuum
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FounderMatchCard(
    match: FounderMatchDTO,
    onClick: () -> Unit,
    onSupportClick: ((String) -> Unit)? = null
) {
    val startupName = match.startupName.orEmpty().ifBlank { "Untitled startup" }
    val startupInitial = startupName.take(1).uppercase()
    val industry = match.industry.orEmpty().ifBlank { "Any industry" }
    val location = match.location.orEmpty().ifBlank { "Location not specified" }
    val fundingStage = match.fundingStage.orEmpty().ifBlank { "Any stage" }
    val description = match.description.orEmpty().trim()

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Almond)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = RussianViolet
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = startupInitial,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = startupName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                        Text(
                            text = industry,
                            style = MaterialTheme.typography.bodySmall,
                            color = CaputMortuum
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Glaucous
                            )
                            Text(
                                text = location,
                                style = MaterialTheme.typography.bodySmall,
                                color = Glaucous
                            )
                            Text("•", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = fundingStage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Glaucous
                            )
                        }
                    }
                }

                Surface(
                    color = when {
                        match.matchScore >= 80 -> Glaucous.copy(alpha = 0.3f)
                        match.matchScore >= 50 -> RussianViolet.copy(alpha = 0.2f)
                        else -> CaputMortuum.copy(alpha = 0.2f)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = RussianViolet
                        )
                        Text(
                            text = "${match.matchScore}%",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                    }
                }
            }

            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RussianViolet.copy(alpha = 0.7f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (match.matchReasons.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Glaucous.copy(alpha = 0.3f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Why this is a good match:",
                        style = MaterialTheme.typography.labelSmall,
                        color = CaputMortuum,
                        fontWeight = FontWeight.SemiBold
                    )
                    match.matchReasons.take(3).forEach { reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Glaucous
                            )
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = RussianViolet
                            )
                        }
                    }
                }
            }

            // Support button for investors
            if (onSupportClick != null) {
                Button(
                    onClick = { onSupportClick(match.founderUserId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RussianViolet)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Text("Support This Startup")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestorMatchCard(
    match: InvestorMatchDTO,
    onClick: () -> Unit,
    onConnectClick: ((String) -> Unit)? = null
) {
    val firmName = match.nameFirm.orEmpty().ifBlank { "Unnamed firm" }
    val firmInitial = firmName.take(1).uppercase()
    val industry = match.industry.orEmpty().ifBlank { "Any industry" }
    val investmentRange = match.investmentRange.orEmpty().ifBlank { "Range not specified" }
    val fundingStagePreference = match.fundingStagePreference.orEmpty().ifBlank { "Any stage" }
    val geographicPreference = match.geographicPreference.orEmpty().ifBlank { "Any location" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Almond)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = Glaucous
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = firmInitial,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = firmName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                        Text(
                            text = industry,
                            style = MaterialTheme.typography.bodySmall,
                            color = CaputMortuum
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.AccountBalance,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Glaucous
                            )
                            Text(
                                text = investmentRange,
                                style = MaterialTheme.typography.bodySmall,
                                color = Glaucous
                            )
                        }
                    }
                }

                Surface(
                    color = when {
                        match.matchScore >= 80 -> Glaucous.copy(alpha = 0.3f)
                        match.matchScore >= 50 -> RussianViolet.copy(alpha = 0.2f)
                        else -> CaputMortuum.copy(alpha = 0.2f)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = RussianViolet
                        )
                        Text(
                            text = "${match.matchScore}%",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = RussianViolet
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        "Funding Stage",
                        style = MaterialTheme.typography.labelSmall,
                        color = CaputMortuum,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        fundingStagePreference,
                        style = MaterialTheme.typography.bodyMedium,
                        color = RussianViolet
                    )
                }
                Column {
                    Text(
                        "Geography",
                        style = MaterialTheme.typography.labelSmall,
                        color = CaputMortuum,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        geographicPreference,
                        style = MaterialTheme.typography.bodyMedium,
                        color = RussianViolet
                    )
                }
            }

            if (match.matchReasons.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Glaucous.copy(alpha = 0.3f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Why this is a good match:",
                        style = MaterialTheme.typography.labelSmall,
                        color = CaputMortuum,
                        fontWeight = FontWeight.SemiBold
                    )
                    match.matchReasons.take(3).forEach { reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Glaucous
                            )
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = RussianViolet
                            )
                        }
                    }
                }
            }

            // Connect button for founders
            if (onConnectClick != null) {
                Button(
                    onClick = { onConnectClick(match.investorUserId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Glaucous)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Text("Connect with Investor")
                    }
                }
            }
        }
    }
}

@Composable
fun AISearchScreenContent(
    searchViewModel: SearchViewModel,
    connectionViewModel: ConnectionViewModel,
    profileViewModel: ProfileViewModel,
    projectViewModel: ProjectViewModel,
    onProfileClick: (String) -> Unit
) {
    AISearchScreen(
        searchViewModel = searchViewModel,
        connectionViewModel = connectionViewModel,
        profileViewModel = profileViewModel,
        projectViewModel = projectViewModel,
        onProfileClick = onProfileClick
    )
}