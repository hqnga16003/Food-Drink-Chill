package com.example.fooddrinkchill.screen.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddrinkchill.R
import com.example.fooddrinkchill.ui.theme.ExtendedTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess:() -> Unit,
    onRegisterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by rememberSaveable() { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is LoginAction.NavigateToHome -> onLoginSuccess()
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(vertical = 48.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!uiState.errorMessage.isNullOrBlank()) {
            MyDialog( title = "Lỗi", message = uiState.errorMessage!!,
                onConfirm = {viewModel.onDismissDialog()},)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_app),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(186.dp)
                    .fillMaxWidth()
            )
        }
        Text(
            text = "Vibe ăn uống đã lên! ✨",
            style = MaterialTheme.typography.headlineMedium,
            color = ExtendedTheme.colorScheme.fixedPrimaryText,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Bật mode login, chốt đơn 'mlem' ngay!",
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Email Address",
            fontSize = 14.sp,
            color = ExtendedTheme.colorScheme.primaryText
        )

        OutlinedTextField(
            value = uiState.input.email,
            onValueChange = { newEmail ->
                viewModel.onEvent(LoginEvent.EmailChanged(newEmail))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            singleLine = true,
            placeholder = { Text("Enter your email") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )


        Text(
            text = "Password",
            fontSize = 14.sp,
            color = ExtendedTheme.colorScheme.primaryText
        )

        OutlinedTextField(
            value = uiState.input.password,
            onValueChange = { newPassword ->
                viewModel.onEvent(LoginEvent.PasswordChanged(newPassword))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            singleLine = true,
            placeholder = { Text("Enter your password") },
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible)
                                R.drawable.ic_visibility_off_24dp
                            else
                                R.drawable.ic_visibility_24dp
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = ExtendedTheme.colorScheme.fixedPrimaryText
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.toggleable(
                    value = uiState.input.rememberMe,
                    onValueChange = { value ->
                        viewModel.onEvent(LoginEvent.RememberMeChanged(value))
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Checkbox(
                        checked = uiState.input.rememberMe,
                        onCheckedChange = { value ->
                            viewModel.onEvent(
                                LoginEvent.RememberMeChanged(value)
                            )
                        }
                    )
                }

                Text("Remember me", color = ExtendedTheme.colorScheme.primaryText)
            }
            Text(
                text = "Forgot password?",
                modifier = Modifier.clickable { Log.d("LOGIN", "Forgot password") },
                fontWeight = FontWeight.Bold,
                color = ExtendedTheme.colorScheme.fixedPrimaryText,
                fontSize = 14.sp
            )
        }
        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.onEvent(LoginEvent.LoginClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Don't have an account? ",
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = "Or sign in with",
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )

            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialButton(
                icon = R.drawable.google,
                text = "Google",
                onClick = { Log.d("LOGIN", "Google clicked") },
                modifier = Modifier.weight(1f)
            )
            SocialButton(
                icon = R.drawable.facebook,
                text = "Facebook",
                onClick = { Log.d("LOGIN", "Facebook clicked") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SocialButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ExtendedTheme.colorScheme.outlineVariant,
        ),
        border = BorderStroke(
            1.dp,
            ExtendedTheme.colorScheme.outline
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium,
            color = ExtendedTheme.colorScheme.primaryText
        )
    }
}

@Composable
fun MyDialog(
    title: String,
    message: String,
    confirmText: String = "Xác nhận",
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
    )
}