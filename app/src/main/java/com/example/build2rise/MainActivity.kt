package com.example.build2rise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.build2rise.ui.InvestorProfilePosts
import com.example.build2rise.ui.FounderProfilePosts

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            MaterialTheme {

                // Toggle user type inside app
                var userType by remember { mutableStateOf("investor") }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //  Toggle UI buttons for testing
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(onClick = { userType = "investor" }) {
                            Text("Investor Mode")
                        }
                        Button(onClick = { userType = "founder" }) {
                            Text("Founder Mode")
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // Show the selected screen
                    if (userType == "investor") {
                        InvestorProfilePosts()
                    } else {
                        FounderProfilePosts()
                    }
                }
            }
        }
    }
}





