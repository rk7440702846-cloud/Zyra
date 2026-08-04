package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    isSignUpMode: Boolean,
    onToggleMode: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ZYRA Logo Badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(ZyraGradientStart, ZyraGradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Z",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isSignUpMode) "Create Zyra Account" else "Welcome Back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ZyraTextPrimary
            )

            Text(
                text = if (isSignUpMode) "Connect with friends & share your moments" else "Log in to your account to continue",
                fontSize = 13.sp,
                color = ZyraTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            // Form Inputs
            if (isSignUpMode) {
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = ZyraTextSecondary) },
                    placeholder = { Text("e.g. Aria Sharma", color = ZyraTextSecondary.copy(0.5f)) },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = ZyraPrimary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ZyraSurface,
                        unfocusedContainerColor = ZyraSurface,
                        focusedBorderColor = ZyraPrimary,
                        unfocusedBorderColor = ZyraCardBorder,
                        focusedTextColor = ZyraTextPrimary,
                        unfocusedTextColor = ZyraTextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = ZyraTextSecondary) },
                    placeholder = { Text("e.g. aria.zyra", color = ZyraTextSecondary.copy(0.5f)) },
                    leadingIcon = { Icon(Icons.Outlined.AlternateEmail, contentDescription = null, tint = ZyraPrimary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ZyraSurface,
                        unfocusedContainerColor = ZyraSurface,
                        focusedBorderColor = ZyraPrimary,
                        unfocusedBorderColor = ZyraCardBorder,
                        focusedTextColor = ZyraTextPrimary,
                        unfocusedTextColor = ZyraTextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = ZyraTextSecondary) },
                placeholder = { Text("user@zyra.com", color = ZyraTextSecondary.copy(0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = ZyraPrimary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ZyraSurface,
                    unfocusedContainerColor = ZyraSurface,
                    focusedBorderColor = ZyraPrimary,
                    unfocusedBorderColor = ZyraCardBorder,
                    focusedTextColor = ZyraTextPrimary,
                    unfocusedTextColor = ZyraTextPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = ZyraTextSecondary) },
                placeholder = { Text("••••••••", color = ZyraTextSecondary.copy(0.5f)) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = ZyraPrimary) },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = ZyraTextSecondary
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ZyraSurface,
                    unfocusedContainerColor = ZyraSurface,
                    focusedBorderColor = ZyraPrimary,
                    unfocusedBorderColor = ZyraCardBorder,
                    focusedTextColor = ZyraTextPrimary,
                    unfocusedTextColor = ZyraTextPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            if (!isSignUpMode) {
                AlignRight {
                    Text(
                        text = "Forgot Password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ZyraSecondary,
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary CTA Button
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(ZyraGradientStart, ZyraGradientEnd)
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = if (isSignUpMode) "Sign Up" else "Log In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider Line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = ZyraCardBorder)
                Text(
                    text = "  OR CONTINUE WITH  ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ZyraTextSecondary
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = ZyraCardBorder)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Social Buttons (Google & Apple)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Google Sign In
                OutlinedButton(
                    onClick = onLoginSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(ZyraCardBorder, ZyraCardBorder))),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ZyraSurface)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "G ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEA4335)
                        )
                        Text(
                            text = "Google",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ZyraTextPrimary
                        )
                    }
                }

                // Apple Sign In
                OutlinedButton(
                    onClick = onLoginSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(ZyraCardBorder, ZyraCardBorder))),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ZyraSurface)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = " ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Apple",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ZyraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Switch Login/SignUp text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSignUpMode) "Already have an account? " else "Don't have an account? ",
                    fontSize = 13.sp,
                    color = ZyraTextSecondary
                )
                Text(
                    text = if (isSignUpMode) "Log In" else "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraSecondary,
                    modifier = Modifier.clickable { onToggleMode() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Guest Explorer Option
            Text(
                text = "Explore as Guest →",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ZyraTextSecondary.copy(alpha = 0.8f),
                modifier = Modifier
                    .clickable { onLoginSuccess() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AlignRight(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        content()
    }
}
