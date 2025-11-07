package com.example.build2rise.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ConnectionItem(
    val name: String,
    val type: String
)

val founderConnections = listOf(
    ConnectionItem("Green Start", "Founder"),
    ConnectionItem("Happy Learning", "Founder"),
    ConnectionItem("Grocery Easy", "Founder"),
    ConnectionItem("Fast Trading", "Founder")
)

val investorConnections = listOf(
    ConnectionItem("Angel Invest", "Investor"),
    ConnectionItem("James Lee", "Investor"),
    ConnectionItem("Emma Ray", "Investor")
)

@Composable
fun Connections() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Connections",
                    color = RussianViolet,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Founders - ${founderConnections.size}",
                fontWeight = FontWeight.Bold,
                color = RussianViolet,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
        }
        items(founderConnections) { connection ->
            ConnectionRow(connection)
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Investors - ${investorConnections.size}",
                fontWeight = FontWeight.Bold,
                color = RussianViolet,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
        }
        items(investorConnections) { connection ->
            ConnectionRow(connection)
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
fun ConnectionRow(connection: ConnectionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(RussianViolet),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = connection.name
                    .split(" ")
                    .take(2)
                    .joinToString("") { it.first().uppercase() },
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = connection.name,
            color = RussianViolet,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Options",
            tint = RussianViolet
        )
    }
}