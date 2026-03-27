package br.me.vitorcsouza.train.ui.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set
    
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> state = state.copy(email = event.email)
            
            is LoginEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            
            is LoginEvent.OnLoginClick -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = loginUseCase(state.email, state.password)
            
            state = if (result.isSuccess) {
                state.copy(isSuccess = true, isLoading = false)
            } else {
                state.copy(error = result.exceptionOrNull()?.message, isLoading = false)
            }
        }
    }
}
