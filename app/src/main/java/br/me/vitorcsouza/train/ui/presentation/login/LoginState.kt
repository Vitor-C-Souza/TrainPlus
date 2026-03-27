package br.me.vitorcsouza.train.ui.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class LoginEvent {
    data class onEmailChanged(val email: String) : LoginEvent()
    data class onPasswordChanged(val password: String) : LoginEvent()
    object onLoginClick : LoginEvent()
}
