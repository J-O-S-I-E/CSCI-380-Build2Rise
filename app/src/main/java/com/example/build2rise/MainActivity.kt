//package com.example.build2rise
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.build2rise.ui.InvestorProfilePosts
//import com.example.build2rise.ui.FounderProfilePosts
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//
//            MaterialTheme {
//
//                // Toggle user type inside app
//                var userType by remember { mutableStateOf("investor") }
//
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    //  Toggle UI buttons for testing
//                    Row(
//                        modifier = Modifier.padding(top = 16.dp),
//                        horizontalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        Button(onClick = { userType = "investor" }) {
//                            Text("Investor Mode")
//                        }
//                        Button(onClick = { userType = "founder" }) {
//                            Text("Founder Mode")
//                        }
//                    }
//
//                    Spacer(Modifier.height(10.dp))
//
//                    // Show the selected screen
//                    if (userType == "investor") {
//                        InvestorProfilePosts()
//                    } else {
//                        FounderProfilePosts()
//                    }
//                }
//            }
//        }
//    }
//}

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
import com.example.build2rise.ui.theme.MainScreen
import com.example.build2rise.ui.auth.AuthNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
//                // Toggle user type for testing
//                var userType by remember { mutableStateOf("founder") }
//
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                ) {
//                    // Testing toggle buttons (remove before demo)
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.spacedBy(10.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Button(onClick = { userType = "investor" }) {
//                            Text("Investor Mode")
//                        }
//                        Button(onClick = { userType = "founder" }) {
//                            Text("Founder Mode")
//                        }
//                    }
//
//                    // Main Screen with Bottom Navigation
//                    MainScreen(userType = userType)
//                }
            Build2RiseApp()
            }
        }
    }
}
@Composable
fun Build2RiseApp() {
    var isAuthenticated by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf("founder") }

    if (isAuthenticated) {
        // main app after authentication
        MainScreen(userType = userType)
        } else {
        // authentication flow
        AuthNavigator(
            onAuthComplete = { selectedUserType ->
                userType = selectedUserType
                isAuthenticated = true
            }
        )
    }
}