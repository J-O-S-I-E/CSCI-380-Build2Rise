package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.build2rise.ui.theme.Almond
import com.example.build2rise.ui.theme.Glaucous
import com.example.build2rise.ui.theme.PureWhite
import com.example.build2rise.ui.theme.RussianViolet
import com.example.build2rise.ui.viewmodel.AuthViewModel
import com.example.build2rise.ui.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecureAccountScreen(
    viewModel: AuthViewModel,
    userType: String,
    onBack: () -> Unit,
    onLoginClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()

    // Show error dialog if registration fails
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Error -> {
                errorMessage = state.message
                showErrorDialog = true
            }
            else -> {}
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Registration Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    viewModel.resetState()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Almond)
    ) {
        // Top Bar with Back Button and Progress
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RussianViolet
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressDot(isActive = false)
                Spacer(modifier = Modifier.width(8.dp))
                ProgressDot(isActive = false)
                Spacer(modifier = Modifier.width(8.dp))
                ProgressDot(isActive = true)
            }

            Spacer(modifier = Modifier.width(48.dp))
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Secure Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = RussianViolet
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your login credentials",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // First Name
                AuthTextField(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "Enter your first name"
                )

                // Last Name
                AuthTextField(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Enter your last name"
                )

                // Email
                Column {
                    AuthTextField(
                        label = "Email",
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = if (it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                                "Invalid email format"
                            } else null
                        },
                        placeholder = "your@email.com"
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                // Password
                Column {
                    Text(
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RussianViolet,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = when {
                                it.length < 6 -> "Password must be at least 6 characters"
                                else -> null
                            }
                        },
                        placeholder = {
                            Text(text = "Create a strong password", color = Color.Gray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                            focusedBorderColor = Glaucous,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                // Confirm Password
                Column {
                    Text(
                        text = "Confirm Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RussianViolet,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = {
                            Text(text = "Confirm your password", color = Color.Gray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                            focusedBorderColor = Glaucous,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = if (showConfirmPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                    if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                        Text(
                            text = "Passwords do not match",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Create Account Button
            Button(
                onClick = {
                    viewModel.register(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        userType = userType
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RussianViolet
                ),
                enabled = firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        password == confirmPassword &&
                        passwordError == null &&
                        emailError == null &&
                        authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = PureWhite,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PureWhite
                    )
                }
            }

            // Login Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already registered?  ",
                    fontSize = 14.sp,
                    color = RussianViolet.copy(alpha = 0.7f)
                )
                TextButton(onClick = onLoginClick) {
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Glaucous
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}