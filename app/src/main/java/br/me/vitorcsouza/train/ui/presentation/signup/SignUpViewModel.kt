package br.me.vitorcsouza.train.ui.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    var state by mutableStateOf(SignUpStates())
        private set

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnUsernameChanged -> {
                state = state.copy(username = event.username)
            }

            is SignUpEvents.OnEmailChanged -> {
                state = state.copy(email = event.email)
            }

            is SignUpEvents.OnPasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is SignUpEvents.OnSignUpClick -> {
                registerUser()
            }

            is SignUpEvents.OnConfirmPasswordChanged -> {
                state = state.copy(confirmPassword = event.confirmPassword)
            }
        }
    }

    private fun registerUser() {
        if (state.username.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            state = state.copy(error = "Please fill in all fields")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val result = registerUseCase(
                username = state.username,
                email = state.email,
                password = state.password
            )

            state = if (result.isSuccess) {
                state.copy(isLoading = false, isSuccess = true)
            } else {
                state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "An unknown error occurred"
                )
            }
        }
    }
}