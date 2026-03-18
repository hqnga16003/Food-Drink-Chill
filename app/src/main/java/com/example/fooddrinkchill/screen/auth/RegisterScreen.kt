package com.example.fooddrinkchill.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddrinkchill.R
import com.example.fooddrinkchill.ui.theme.ExtendedTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
    onBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is RegisterAction.NavigateToHome -> onRegisterSuccess()
            }
        }
    }
    if (!uiState.errorMessage.isNullOrBlank()) {
        MyDialog(
            title = "Lỗi", message = uiState.errorMessage!!,
            onConfirm = { viewModel.onDismissDialog() },
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(targetState = uiState.step, label = "RegisterStep") { step ->
            when (step) {
                1 -> RegisterForm(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onBackToLogin = onBackToLogin
                )

                2 -> EmailVerification(
                    email = uiState.input.email,
                    onEvent = viewModel::onEvent,
                    onRegisterSuccess = onRegisterSuccess
                )
            }
        }
    }
}

@Composable
fun RegisterForm(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit,
    onBackToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isPasswordFocused by remember { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .clickable { onBackToLogin() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_visibility_24dp),
                    contentDescription = null,
                    tint = ExtendedTheme.colorScheme.fixedPrimaryText
                )
            }

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Tham gia cùng FoodDrinkChill! ✨",
                style = MaterialTheme.typography.headlineMedium,
                color = ExtendedTheme.colorScheme.fixedPrimaryText,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tạo tài khoản để khám phá thế giới mlem mlem",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            StepIndicator(currentStep = 1, totalSteps = 2)

            Spacer(modifier = Modifier.height(20.dp))

            FieldLabel("Email Address")
            OutlinedTextField(
                value = uiState.input.email,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                singleLine = true,
                placeholder = { Text("Enter your email") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            FieldLabel("Password")
            OutlinedTextField(
                value = uiState.input.password,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isPasswordFocused = it.isFocused },
                shape = RoundedCornerShape(50),
                singleLine = true,
                placeholder = { Text("Enter your password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.ic_visibility_off_24dp
                                else R.drawable.ic_visibility_24dp
                            ),
                            contentDescription = null,
                            tint = ExtendedTheme.colorScheme.fixedPrimaryText
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            AnimatedVisibility(
                visible = isPasswordFocused,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(durationMillis = 300),
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 300),
                    shrinkTowards = Alignment.Top,
                    clip = false
                ) + fadeOut()
            ) {
                ValidationMessages(
                    rules = uiState.input.passwordRules,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            FieldLabel("Confirm Password")
            OutlinedTextField(
                value = uiState.input.confirmPassword,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                singleLine = true,
                placeholder = { Text("Enter your password again") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(if (confirmPasswordVisible) R.drawable.ic_visibility_off_24dp else R.drawable.ic_visibility_24dp),
                            contentDescription = null,
                            tint = ExtendedTheme.colorScheme.fixedPrimaryText
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onEvent(RegisterEvent.RegisterClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Log in",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = ExtendedTheme.colorScheme.primaryText,
        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
    )
}

@Composable
private fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            val active = index + 1 == currentStep
            val done = index + 1 < currentStep
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .then(
                        if (active) Modifier.fillMaxWidth(0.18f) else Modifier.size(
                            width = 24.dp,
                            height = 4.dp
                        )
                    )
                    .clip(CircleShape)
                    .background(
                        when {
                            active -> MaterialTheme.colorScheme.primary
                            done -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }
                    )
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "Bước $currentStep/$totalSteps",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun EmailVerification(
    email: String,
    onEvent: (RegisterEvent) -> Unit,
    onRegisterSuccess:() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "2/2",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.size(38.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape).border(
                        0.5.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_email_24dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Xác nhận email của bạn",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chúng tôi đã gửi liên kết xác nhận\ntới địa chỉ email sau",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .border(
                        0.5.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(40.dp)
                    )
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_email_24dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "i",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Kiểm tra cả thư mục Spam nếu bạn không thấy email trong hộp thư đến.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.5f
                )
            }
            Button(
                onClick = onRegisterSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Tiếp tục",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chưa nhận được email? ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = "Gửi lại",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.clickable { onEvent(RegisterEvent.ResendEmailClicked) }
                )
            }
        }
    }
}


@Composable
fun ValidationMessages(
    rules: List<PasswordRuleState>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rules.forEach { rule ->
            val color = if (rule.isValid)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )

                Text(
                    text = rule.message,
                    color = color,
                    fontSize = 13.sp
                )
            }
        }
    }
}