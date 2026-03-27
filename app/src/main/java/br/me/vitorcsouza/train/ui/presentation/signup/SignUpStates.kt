package br.me.vitorcsouza.train.ui.presentation.signup

data class SignUpStates(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class SignUpEvents {
    data class OnUsernameChanged(val username: String) : SignUpEvents()
    data class OnEmailChanged(val email: String) : SignUpEvents()
    data class OnPasswordChanged(val password: String) : SignUpEvents()
    data class OnConfirmPasswordChanged(val confirmPassword: String) : SignUpEvents()
    object OnSignUpClick : SignUpEvents()
}
