package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.Glaucous
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet

@Composable
fun SuccessScreen(
    onGetStarted: () -> Unit,
    onCompleteProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RussianViolet)
    ) {
        // Top Section - Success Message
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon
            Text(
                text = "✓",
                fontSize = 80.sp,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Welcome to Build2Rise",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your account has been created successfully",
                fontSize = 16.sp,
                color = PureWhite.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }

        // Bottom Section - Action Items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(
                    PureWhite,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Almond
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "You're all set!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RussianViolet
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start exploring the platform. You can add more details to your profile anytime to increase visibility.",
                        fontSize = 14.sp,
                        color = RussianViolet.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Text(
                text = "Quick Actions:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickAction("Browse startup pitches")
                QuickAction("Discover investors")
                QuickAction("Post your first update")
                QuickAction("Complete profile")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RussianViolet
                    )
                ) {
                    Text(
                        text = "Build2Rise",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PureWhite
                    )
                }

                TextButton(
                    onClick = onCompleteProfile,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Complete profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Glaucous
                    )
                }
            }
        }
    }
}

@Composable
fun QuickAction(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "•",
            fontSize = 16.sp,
            color = RussianViolet
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = RussianViolet.copy(alpha = 0.8f)
        )
    }
}