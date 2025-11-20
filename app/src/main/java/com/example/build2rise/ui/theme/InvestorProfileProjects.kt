package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.build2rise.data.model.ProjectResponse
import com.example.build2rise.ui.viewmodel.ProjectViewModel
import com.example.build2rise.ui.viewmodel.ProjectsState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun InvestorProfileProjects(
    viewModel: ProjectViewModel = viewModel()
) {
    val projectsState by viewModel.projectsState.collectAsState()

    // Load projects
    LaunchedEffect(Unit) {
        viewModel.getInvestorProjects()
    }

    when (val state = projectsState) {
        is ProjectsState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RussianViolet)
            }
        }
        is ProjectsState.Success -> {
            if (state.projects.projects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "No supported projects yet",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            "Use AI Search to find founders and support their projects",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(state.projects.projects) { project ->
                        SupportedProjectCard(project)
                    }
                }
            }
        }
        is ProjectsState.Error -> {
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
                        onClick = { viewModel.getInvestorProjects() },
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

@Composable
fun SupportedProjectCard(project: ProjectResponse) {
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
                    val initials = project.founderName
                        ?.split(" ")
                        ?.take(2)
                        ?.joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
                        ?: "F"
                    Text(
                        text = initials,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.width(16.dp))

                // Founder Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.founderStartupName ?: project.founderName ?: "Startup",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                    Text(
                        text = project.founderName ?: "Founder",
                        fontSize = 14.sp,
                        color = CaputMortuum
                    )
                    if (project.founderIndustry != null) {
                        Text(
                            text = project.founderIndustry,
                            fontSize = 12.sp,
                            color = Glaucous
                        )
                    }
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (project.status) {
                        "funded" -> Glaucous.copy(alpha = 0.2f)
                        "supporting" -> CaputMortuum.copy(alpha = 0.2f)
                        else -> Color.Gray.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = project.status.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = RussianViolet,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Supporting since date
            Text(
                text = "Supporting ${formatProjectDate(project.createdAt)}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

fun formatProjectDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter
            .ofPattern("MMM yyyy")
            .withZone(ZoneId.systemDefault())
        "since ${formatter.format(instant)}"
    } catch (e: Exception) {
        "recently"
    }
}