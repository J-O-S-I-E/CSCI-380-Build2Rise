package com.example.build2rise.ui.theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



data class InvestorProject(
    val initials: String,
    val company: String,
    val founderInfo: String,
    val description: String
)

val sampleInvestorProjects = listOf(
    InvestorProject("GS", "GreenStart", "Founder, CleanTech", "Product description"),
    InvestorProject("TC", "TechCorp", "Founder, FinTech", "Product description")
)

@Composable
fun InvestorProfileProjects() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(horizontal = 16.dp)
    ) {
        sampleInvestorProjects.forEach { project ->
            ProjectCard(project)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProjectCard(project: InvestorProject) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .border(1.dp, Color(0xFFE4DBD1), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(RussianViolet),
                contentAlignment = Alignment.Center
            ) {
                Text(project.initials, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(project.company, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = RussianViolet)
                Text(project.founderInfo, color = Color.Gray, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Almond),
            contentAlignment = Alignment.Center
        ) {
            Text("video", color = RussianViolet)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(project.description, color = CaputMortuum, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "Like",
                    tint = RussianViolet
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Comment,
                    contentDescription ="Comment",
                    tint = RussianViolet
                )
            }
        }
    }
}



