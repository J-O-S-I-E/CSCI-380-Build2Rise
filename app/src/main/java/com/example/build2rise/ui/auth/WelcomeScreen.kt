package com.example.build2rise.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.Glaucous
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Almond)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo and Title
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Logo placeholder - replace with actual logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Coins icon placeholder
                Text(
                    text = "💰",
                    fontSize = 48.sp
                )
                Text(
                    text = "Build2Rise",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
            }
        }

        // Illustration placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🚀",
                fontSize = 80.sp
            )
        }

        // Bottom Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Your digital Shark Tank",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Build2Rise connects founders ready to pitch with investors seeking the next big opportunity.",
                    fontSize = 16.sp,
                    color = RussianViolet.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                Text(
                    text = "All In One Digital Space",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = RussianViolet.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }

            // Get Started Button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Glaucous
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PureWhite
                )
            }

            // Login Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Already have an account?  ",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f)
                )
                TextButton(onClick = onLogin) {
                    Text(
                        text = "Log In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Glaucous
                    )
                }
            }
        }
    }
}