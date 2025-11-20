package com.example.build2rise.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.data.model.UserProfileResponse

@Composable
fun FounderProfileProjects(
    profile: UserProfileResponse
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                    .padding(20.dp)
            ) {
                // Project Name (Startup Name)
                Text(
                    text = profile.profileData?.startupName ?: "My Startup",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )

                Spacer(Modifier.height(12.dp))

                // Industry & Funding Stage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Industry Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Glaucous.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = profile.profileData?.industry ?: "Tech",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = RussianViolet,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Funding Stage Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CaputMortuum.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = profile.profileData?.fundingStage ?: "Seed",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = RussianViolet,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Description
                Text(
                    text = "About",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = profile.profileData?.description ?: "No description provided.",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))

                // Additional Info
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow(
                        icon = "📍",
                        label = "Location",
                        value = profile.profileData?.location ?: "Not specified"
                    )
                    InfoRow(
                        icon = "👥",
                        label = "Team Size",
                        value = profile.profileData?.teamSize ?: "Not specified"
                    )
                    InfoRow(
                        icon = "💰",
                        label = "Funding Stage",
                        value = profile.profileData?.fundingStage ?: "Not specified"
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 16.sp
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$label:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CaputMortuum,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = RussianViolet
        )
    }
}