package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.Glaucous
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FounderProfileFormScreen(
    onBack: () -> Unit,
    onContinue: (FounderFormData) -> Unit
) {
    var startupName by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var teamSize by remember { mutableStateOf("") }
    var fundingStage by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Almond)
    ) {
        // Top Bar with Back Button and Progress
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RussianViolet
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressDot(isActive = false)
                Spacer(modifier = Modifier.width(8.dp))
                ProgressDot(isActive = true)
                Spacer(modifier = Modifier.width(8.dp))
                ProgressDot(isActive = false)
            }

            Spacer(modifier = Modifier.width(48.dp))
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Founder Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tell us about your venture",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Startup Name
                AuthTextField(
                    label = "Startup Name",
                    value = startupName,
                    onValueChange = { startupName = it },
                    placeholder = "Startup/Company Name"
                )

                // Industry
                AuthDropdownField(
                    label = "Industry",
                    selectedValue = industry,
                    onValueChange = { industry = it },
                    placeholder = "Select Industry",
                    options = listOf(
                        "Fintech", "HealthTech", "EdTech", "CleanTech",
                        "E-commerce", "SaaS", "AI/ML", "Other"
                    )
                )

                // Location
                AuthDropdownField(
                    label = "Location",
                    selectedValue = location,
                    onValueChange = { location = it },
                    placeholder = "Select Location",
                    options = listOf(
                        // === NORTH AMERICA ===
                        "🇺🇸 New York, NY",
                        "🇺🇸 San Francisco, CA",
                        "🇺🇸 Boston, MA",
                        "🇺🇸 Austin, TX",
                        "🇺🇸 Seattle, WA",
                        "🇺🇸 Los Angeles, CA",
                        "🇺🇸 Chicago, IL",
                        "🇺🇸 Miami, FL",
                        "🇨🇦 Toronto, Canada",
                        "🇨🇦 Vancouver, Canada",

                        // === EUROPE ===
                        "🇬🇧 London, UK",
                        "🇩🇪 Berlin, Germany",
                        "🇩🇪 Munich, Germany",
                        "🇳🇱 Amsterdam, Netherlands",
                        "🇫🇷 Paris, France",
                        "🇪🇸 Barcelona, Spain",
                        "🇸🇪 Stockholm, Sweden",
                        "🇮🇪 Dublin, Ireland",
                        "🇨🇭 Zurich, Switzerland",

                        // === ASIA ===
                        "🇸🇬 Singapore",
                        "🇯🇵 Tokyo, Japan",
                        "🇰🇷 Seoul, South Korea",
                        "🇭🇰 Hong Kong",
                        "🇮🇳 Bangalore, India",
                        "🇮🇱 Tel Aviv, Israel",
                        "🇦🇪 Dubai, UAE",

                        // === OTHER ===
                        "🇦🇺 Sydney, Australia",
                        "🇧🇷 São Paulo, Brazil",
                        "🇿🇦 Cape Town, South Africa",
                        "🌍 Remote"
                    )
                )

                // Team Size
                AuthDropdownField(
                    label = "Team Size",
                    selectedValue = teamSize,
                    onValueChange = { teamSize = it },
                    placeholder = "Select Team Size",
                    options = listOf(
                        "1-5", "6-10", "11-20", "21-50", "50+"
                    )
                )

                // Funding Stage
                AuthDropdownField(
                    label = "Funding Stage",
                    selectedValue = fundingStage,
                    onValueChange = { fundingStage = it },
                    placeholder = "Select Funding Stage",
                    options = listOf(
                        "Idea/Pre-seed", "Seed", "Series A",
                        "Series B", "Series C+", "Bootstrapped", "Growth Stage"
                    )
                )

                // Startup Description
                Column {
                    Text(
                        text = "Startup Description",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RussianViolet,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = {
                            Text(
                                text = "Brief description of your startup...",
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                            focusedBorderColor = Glaucous,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            Button(
                onClick = {
                    onContinue(
                        FounderFormData(
                            startupName = startupName,
                            industry = industry.takeIf { it.isNotBlank() },
                            location = location.takeIf { it.isNotBlank() },
                            teamSize = teamSize.takeIf { it.isNotBlank() },
                            fundingStage = fundingStage.takeIf { it.isNotBlank() },
                            description = description.takeIf { it.isNotBlank() }
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RussianViolet
                ),
                enabled = startupName.isNotBlank()
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PureWhite
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
