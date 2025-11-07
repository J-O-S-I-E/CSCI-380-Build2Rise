package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet

@Composable
fun InvestorProfileScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    var nameFirm by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var geographicPreference by remember { mutableStateOf("") }
    var investmentRange by remember { mutableStateOf("") }
    var fundingStage by remember { mutableStateOf("") }

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
                    imageVector = Icons.Default.ArrowBack,
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

            // Empty space for symmetry
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
                    text = "Investor Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Share your investment preferences",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Name/Firm
                AuthTextField(
                    label = "Name/Firm",
                    value = nameFirm,
                    onValueChange = { nameFirm = it },
                    placeholder = "Startup/Company Name"
                )

                // Industry
                AuthDropdownField(
                    label = "Industry",
                    placeholder = "Select Industry",
                    options = listOf(
                        "All Industries",
                        "Fintech",
                        "HealthTech",
                        "EdTech",
                        "CleanTech",
                        "E-commerce",
                        "SaaS",
                        "AI/ML",
                        "Other"
                    )
                )

                // Geographic Preference
                AuthDropdownField(
                    label = "Geographic Preference",
                    placeholder = "Select Location",
                    options = listOf(
                        "Global",
                        "United States",
                        "North America",
                        "Europe",
                        "Asia",
                        "Latin America",
                        "Africa",
                        "Remote Only"
                    )
                )

                // Investment Range
                AuthDropdownField(
                    label = "Investment Range",
                    placeholder = "Select Team Size",
                    options = listOf(
                        "$10K - $50K",
                        "$50K - $100K",
                        "$100K - $250K",
                        "$250K - $500K",
                        "$500K - $1M",
                        "$1M - $5M",
                        "$5M+"
                    )
                )

                // Funding Stage
                AuthDropdownField(
                    label = "Funding Stage",
                    placeholder = "Select Funding Stage",
                    options = listOf(
                        "All Stages",
                        "Idea/Pre-seed",
                        "Seed",
                        "Series A",
                        "Series B",
                        "Series C+",
                        "Growth Stage"
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RussianViolet
                ),
                enabled = nameFirm.isNotBlank()
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