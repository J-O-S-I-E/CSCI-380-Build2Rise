package com.example.build2rise.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecureAccountScreen(
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

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
                    imageVector = Icons.Default.ArrowBack,
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

            // Empty space for symmetry
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
                // Full Name
                AuthTextField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Enter your full name"
                )

                // Email
                AuthTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "your@email.com"
                )

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
                        onValueChange = { password = it },
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
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Create Account Button
            Button(
                onClick = onCreateAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RussianViolet
                ),
                enabled = fullName.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        password == confirmPassword
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PureWhite
                )
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