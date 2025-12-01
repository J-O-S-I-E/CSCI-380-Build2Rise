package com.example.build2rise.ui.auth

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.build2rise.ui.viewmodel.AuthViewModel
import com.example.build2rise.ui.viewmodel.ProfileViewModel
import com.example.build2rise.ui.viewmodel.AuthState
import com.example.build2rise.ui.viewmodel.ProfileCreateState


/**
 * Central authentication navigation manager with backend integration
 */
@Composable
fun AuthNavigator(
    onAuthComplete: (userType: String) -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<AuthScreen>(AuthScreen.Welcome) }
    var selectedUserType by remember { mutableStateOf<String?>(null) }

    // Profile form data
    var profileData by remember { mutableStateOf<ProfileFormData?>(null) }

    // Observe auth state
    val authState by authViewModel.authState.collectAsState()
    val profileCreateState by profileViewModel.profileCreateState.collectAsState()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                // Auth successful, navigate to success screen
                currentScreen = AuthScreen.Success
                selectedUserType = state.userType
                authViewModel.resetState()
            }
            is AuthState.Error -> {
                // Error will be shown in the UI screens
            }
            else -> {}
        }
    }

    // ✅ Handle profile creation state
    LaunchedEffect(profileCreateState) {
        when (profileCreateState) {
            is ProfileCreateState.Success -> {
                // Profile created successfully, navigate to main app
                profileViewModel.resetCreateState()
                onAuthComplete(selectedUserType ?: "founder")
            }
            is ProfileCreateState.Error -> {
                // Error shown in UI, stay on success screen
            }
            else -> {}
        }
    }

    when (currentScreen) {
        AuthScreen.Welcome -> {
            WelcomeScreen(
                onGetStarted = { currentScreen = AuthScreen.CreateAccount },
                onLogin = { currentScreen = AuthScreen.Login }
            )
        }

        AuthScreen.CreateAccount -> {
            CreateAccountScreen(
                onBackClick = { currentScreen = AuthScreen.Login },
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
            FounderProfileFormScreen(
                onBack = { currentScreen = AuthScreen.CreateAccount },
                onContinue = { founderData ->
                    profileData = ProfileFormData(founderData = founderData)
                    currentScreen = AuthScreen.SecureAccount
                }
            )
        }

        AuthScreen.InvestorProfile -> {
            InvestorProfileFormScreen(
                onBack = { currentScreen = AuthScreen.CreateAccount },
                onContinue = { investorData ->
                    profileData = ProfileFormData(investorData = investorData)
                    currentScreen = AuthScreen.SecureAccount
                }
            )
        }

        AuthScreen.SecureAccount -> {
            SecureAccountScreen(
                viewModel = authViewModel,
                userType = selectedUserType ?: "founder",
                onBack = {
                    currentScreen = if (selectedUserType == "founder") {
                        AuthScreen.FounderProfile
                    } else {
                        AuthScreen.InvestorProfile
                    }
                },
                onLoginClick = { currentScreen = AuthScreen.Login }
            )
        }

        AuthScreen.Success -> {
            SuccessScreen(
                isLoading = profileCreateState is ProfileCreateState.Loading,
                onGetStarted = {
                    // ✅ Create profile with saved data, then navigate
                    profileData?.let { data ->
                        if (selectedUserType == "founder") {
                            profileViewModel.createFounderProfile(
                                startupName = data.founderData?.startupName ?: "",
                                industry = data.founderData?.industry,
                                location = data.founderData?.location,
                                teamSize = data.founderData?.teamSize,
                                fundingStage = data.founderData?.fundingStage,
                                description = data.founderData?.description
                            )
                        } else {
                            profileViewModel.createInvestorProfile(
                                nameFirm = data.investorData?.nameFirm ?: "",
                                industry = data.investorData?.industry,
                                geographicPreference = data.investorData?.geographicPreference,
                                investmentRange = data.investorData?.investmentRange,
                                fundingStagePreference = data.investorData?.fundingStagePreference
                            )
                        }
                    } ?: run {
                        // ❌ No profile data saved, navigate anyway (shouldn't happen)
                        onAuthComplete(selectedUserType ?: "founder")
                    }
                }
                // ❌ REMOVED: onCompleteProfile parameter
            )
        }

        AuthScreen.Login -> {
            LoginScreen(
                viewModel = authViewModel,
                onBack = { currentScreen = AuthScreen.Welcome },
                onSignUp = { currentScreen = AuthScreen.CreateAccount },
                onForgotPassword = {
                    // TODO: Implement forgot password
                }
            )
        }
    }
}

/**
 * Data class to hold profile form data between screens
 */
data class ProfileFormData(
    val founderData: FounderFormData? = null,
    val investorData: InvestorFormData? = null
)

data class FounderFormData(
    val startupName: String,
    val industry: String?,
    val location: String?,
    val teamSize: String?,
    val fundingStage: String?,
    val description: String?
)

data class InvestorFormData(
    val nameFirm: String,
    val industry: String?,
    val geographicPreference: String?,
    val investmentRange: String?,
    val fundingStagePreference: String?
)

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