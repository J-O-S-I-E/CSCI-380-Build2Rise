package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.Glaucous
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet

@Composable
fun CreateAccountScreen(
    onFounderSelected: () -> Unit,
    onInvestorSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Almond)
    ) {
        // Progress Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProgressDot(isActive = true)
            Spacer(modifier = Modifier.width(8.dp))
            ProgressDot(isActive = false)
            Spacer(modifier = Modifier.width(8.dp))
            ProgressDot(isActive = false)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Title Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose your path",
                fontSize = 16.sp,
                color = RussianViolet.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Join Build2Rise",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = RussianViolet,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Connect, pitch and grow your startup journey",
                fontSize = 16.sp,
                color = RussianViolet.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Founder Card
            UserTypeCard(
                icon = "💡",
                title = "Founder",
                description = "Looking to pitch and connect with investors",
                onClick = onFounderSelected
            )

            // Investor Card
            UserTypeCard(
                icon = "🏛️",
                title = "Investor",
                description = "Seeking promising startups to invest in",
                onClick = onInvestorSelected
            )
        }
    }
}

@Composable
fun ProgressDot(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(if (isActive) 40.dp else 12.dp)
            .height(12.dp)
            .background(
                color = if (isActive) Glaucous else Color.LightGray,
                shape = RoundedCornerShape(6.dp)
            )
    )
}

@Composable
fun UserTypeCard(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = RussianViolet.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Almond, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )
            }

            // Text Content
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}