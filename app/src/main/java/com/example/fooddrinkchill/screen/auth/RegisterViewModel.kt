package com.example.fooddrinkchill.screen.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddrinkchill.data.model.User
import com.example.fooddrinkchill.data.repository.FirebaseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.fooddrinkchill.data.model.Result

class RegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _action = Channel<RegisterAction>()
    val action = _action.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        input = it.input.copy(email = event.email),
                        errorMessage = null
                    )
                }
            }

            is RegisterEvent.PasswordChanged -> {
                onPasswordChanged(event.password)
            }

            is RegisterEvent.ConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        input = it.input.copy(confirmPassword = event.confirmPassword),
                        errorMessage = null
                    )
                }
            }

            RegisterEvent.RegisterClicked -> handleRegister()
            RegisterEvent.BackToFormClicked -> {
                _uiState.update { it.copy(step = 1) }
            }
        }
    }

    private fun handleRegister() {
        val state = _uiState.value

        val email = _uiState.value.input.email.trim()
        val password = _uiState.value.input.password.trim()
        val confirmPassword = _uiState.value.input.confirmPassword.trim()

        if (email.isBlank() || password.isBlank()
            || confirmPassword.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return
        }
        if (state.input.password != state.input.confirmPassword) {
            return _uiState.update { it.copy(errorMessage = "Password không khớp") }
        }
        if (!isValidEmail(state.input.email)) {
            return _uiState.update { it.copy(errorMessage = "Vui lòng nhập đúng email") }
        }
        val allRulesSatisfied = state.input.passwordRules.all { it.isValid }
        if (!allRulesSatisfied) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu chưa đáp ứng đủ các yêu cầu bảo mật") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result: Result<User> = repository.register(
                state.input.email,
                state.input.password
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(step = 2) }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = result.message)
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun evaluatePassword(password: String): List<PasswordRuleState> {
        return PasswordRules.rules.map { rule ->
            PasswordRuleState(
                message = rule.message,
                isValid = rule.validator(password)
            )
        }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                input = it.input.copy(
                    password = password,
                    passwordRules = evaluatePassword(password)
                ),
            )
        }
    }
}

data class RegisterUiState(
    val input: RegisterInput = RegisterInput(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val step: Int = 1
)

data class RegisterInput(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordRules: List<PasswordRuleState> = PasswordRules.rules.map {
        PasswordRuleState(
            message = it.message,
            isValid = false
        )
    }
)

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object BackToFormClicked : RegisterEvent()
}

sealed interface RegisterAction {
    object NavigateToHome : RegisterAction
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

data class PasswordRuleState(
    val message: String,
    val isValid: Boolean
)

data class PasswordRule(
    val id: String,
    val message: String,
    val validator: (String) -> Boolean
)

object PasswordRules {
    val rules = listOf(
        PasswordRule(
            id = "length",
            message = "Ít nhất 8 ký tự"
        ) { it.length >= 8 },

        PasswordRule(
            id = "uppercase",
            message = "Có chữ hoa"
        ) { it.any { c -> c.isUpperCase() } },

        PasswordRule(
            id = "number",
            message = "Có số"
        ) { it.any { c -> c.isDigit() } },

        PasswordRule(
            id = "special",
            message = "Có ký tự đặc biệt"
        ) { it.any { !it.isLetterOrDigit() } }
    )
}