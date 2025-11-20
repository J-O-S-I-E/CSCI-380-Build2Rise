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
//import com.example.build2rise.ui.theme.MainScreen
//import com.example.build2rise.ui.auth.AuthNavigator
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//            Build2RiseApp()
//            }
//        }
//    }
//}
//@Composable
//fun Build2RiseApp() {
//    var isAuthenticated by remember { mutableStateOf(false) }
//    var userType by remember { mutableStateOf("founder") }
//
//    if (isAuthenticated) {
//        // main app after authentication
//        MainScreen(userType = userType)
//        } else {
//        // authentication flow
//        AuthNavigator(
//            onAuthComplete = { selectedUserType ->
//                userType = selectedUserType
//                isAuthenticated = true
//            }
//        )
//    }
//}

//package com.example.build2rise
//
//import android.content.Context
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.build2rise.ui.theme.MainScreen
//import com.example.build2rise.ui.auth.AuthNavigator
//import com.example.build2rise.ui.viewmodel.AuthViewModel
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                Build2RiseApp()
//            }
//        }
//    }
//}
//
//@Composable
//fun Build2RiseApp(
//    authViewModel: AuthViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    var userType by remember { mutableStateOf<String?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var isLoggedIn by remember { mutableStateOf(false) }
//
//    // Check for existing auth token and user type on startup
//    LaunchedEffect(Unit) {
//        scope.launch {
//            // Check SharedPreferences directly for most reliable state
//            val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
//            val token = prefs.getString("auth_token", null)
//            val savedUserType = prefs.getString("user_type", null)
//
//            isLoggedIn = token != null
//            userType = savedUserType
//            isLoading = false
//
//            // Also update ViewModel state to match
//            if (isLoggedIn) {
//                authViewModel.checkAuthStatus()
//            }
//        }
//    }
//
//    when {
//        isLoading -> {
//            // Show loading screen while checking auth
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//
//        isLoggedIn && userType != null -> {
//            // User is authenticated, show main app
//            MainScreen(userType = userType!!)
//        }
//
//        else -> {
//            // User not authenticated, show auth flow
//            AuthNavigator(
//                authViewModel = authViewModel,
//                onAuthComplete = { selectedUserType ->
//                    userType = selectedUserType
//                    isLoggedIn = true
//                }
//            )
//        }
//    }
//}

package com.example.build2rise

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.ui.theme.MainScreen
import com.example.build2rise.ui.auth.AuthNavigator
import com.example.build2rise.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Build2RiseApp()
            }
        }
    }
}

@Composable
fun Build2RiseApp(
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var userType by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(false) }

    // Check for existing auth token and user type on startup
    LaunchedEffect(Unit) {
        scope.launch {
            // Check SharedPreferences directly for most reliable state
            val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = prefs.getString("auth_token", null)
            val savedUserType = prefs.getString("user_type", null)

            isLoggedIn = token != null
            userType = savedUserType
            isLoading = false
        }
    }

    when {
        isLoading -> {
            // Show loading screen while checking auth
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        isLoggedIn && userType != null -> {
            // User is authenticated, show main app
            MainScreen(userType = userType!!)
        }

        else -> {
            // User not authenticated, show auth flow
            AuthNavigator(
                authViewModel = authViewModel,
                onAuthComplete = { selectedUserType ->
                    userType = selectedUserType
                    isLoggedIn = true
                }
            )
        }
    }
}