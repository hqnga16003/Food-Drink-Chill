package com.example.fooddrinkchill.screen.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddrinkchill.data.model.User
import com.example.fooddrinkchill.data.repository.FirebaseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.fooddrinkchill.data.model.Result

class LoginViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _action = Channel<LoginAction>()
    val action = _action.receiveAsFlow()

    private fun handleLoginClicked() {
        val email = _uiState.value.input.email.trim()
        val password = _uiState.value.input.password.trim()

        if (_uiState.value.isLoading) return
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Email không hợp lệ") }
            return
        }
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email và mật khẩu không được để trống") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result: Result<User> = repository.login(email, password)) {
                is Result.Success -> {
                    _action.send(LoginAction.NavigateToHome)
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = result.message)
                    }
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun handleEmailChanged(email: String) {
        _uiState.update { state ->
            state.copy(
                input = state.input.copy(
                    email = email
                )
            )
        }
    }

    private fun handlePasswordChanged(password: String) {
        _uiState.update { state ->
            state.copy(
                input = state.input.copy(
                    password = password
                )
            )
        }
    }

    private fun handleRememberClicked(remember: Boolean) {
        _uiState.update { state ->
            state.copy(
                input = state.input.copy(
                    rememberMe = remember
                )
            )
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                handleEmailChanged(event.email)
            }

            is LoginEvent.PasswordChanged -> {
                handlePasswordChanged(event.password)
            }

            is LoginEvent.RememberMeChanged -> {
                handleRememberClicked(event.rememberMe)
            }

            is LoginEvent.LoginClicked -> handleLoginClicked()
        }
    }

}

data class LoginUiState(
    val input: LoginInput = LoginInput(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailValidate: String? = null,
)

data class LoginInput(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class RememberMeChanged(val rememberMe: Boolean) : LoginEvent()
    object LoginClicked : LoginEvent()
}

sealed interface LoginAction {
    object NavigateToHome : LoginAction
}
