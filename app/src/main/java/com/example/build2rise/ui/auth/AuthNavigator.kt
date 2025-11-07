package com.example.build2rise.ui.auth

import androidx.compose.runtime.*
import com.example.build2rise.ui.theme.MainScreen

/**
 * Central authentication navigation manager
 * Handles all auth screen transitions
 */
@Composable
fun AuthNavigator(
    onAuthComplete: (userType: String) -> Unit
) {
    var currentScreen by remember { mutableStateOf<AuthScreen>(AuthScreen.Welcome) }
    var selectedUserType by remember { mutableStateOf<String?>(null) }

    when (currentScreen) {
        AuthScreen.Welcome -> {
            WelcomeScreen(
                onGetStarted = { currentScreen = AuthScreen.CreateAccount },
                onLogin = { currentScreen = AuthScreen.Login }
            )
        }

        AuthScreen.CreateAccount -> {
            CreateAccountScreen(
                onFounderSelected = {
                    selectedUserType = "founder"
                    currentScreen = AuthScreen.FounderProfile
                },
                onInvestorSelected = {
                    selectedUserType = "investor"
                    currentScreen = AuthScreen.InvestorProfile
                }
            )
        }

        AuthScreen.FounderProfile -> {
            FounderProfileScreen(
                onBack = { currentScreen = AuthScreen.CreateAccount },
                onContinue = { currentScreen = AuthScreen.SecureAccount }
            )
        }

        AuthScreen.InvestorProfile -> {
            InvestorProfileScreen(
                onBack = { currentScreen = AuthScreen.CreateAccount },
                onContinue = { currentScreen = AuthScreen.SecureAccount }
            )
        }

        AuthScreen.SecureAccount -> {
            SecureAccountScreen(
                onBack = {
                    currentScreen = if (selectedUserType == "founder") {
                        AuthScreen.FounderProfile
                    } else {
                        AuthScreen.InvestorProfile
                    }
                },
                onCreateAccount = { currentScreen = AuthScreen.Success },
                onLoginClick = { currentScreen = AuthScreen.Login }
            )
        }

        AuthScreen.Success -> {
            SuccessScreen(
                onGetStarted = {
                    // Navigate to main app with selected user type
                    onAuthComplete(selectedUserType ?: "founder")
                },
                onCompleteProfile = {
                    // For now, also navigate to main app
                    // Later, you can add profile completion screens
                    onAuthComplete(selectedUserType ?: "founder")
                }
            )
        }

        AuthScreen.Login -> {
            LoginScreen(
                onBack = { currentScreen = AuthScreen.Welcome },
                onLogin = {
                    // In Sprint 2, validate credentials here
                    // For now, navigate to main app
                    onAuthComplete(selectedUserType ?: "founder")
                },
                onSignUp = { currentScreen = AuthScreen.CreateAccount },
                onForgotPassword = {
                    // TODO: Implement forgot password flow in Sprint 2
                }
            )
        }
    }
}

/**
 * Sealed class representing all authentication screens
 */
sealed class AuthScreen {
    object Welcome : AuthScreen()
    object CreateAccount : AuthScreen()
    object FounderProfile : AuthScreen()
    object InvestorProfile : AuthScreen()
    object SecureAccount : AuthScreen()
    object Success : AuthScreen()
    object Login : AuthScreen()
}