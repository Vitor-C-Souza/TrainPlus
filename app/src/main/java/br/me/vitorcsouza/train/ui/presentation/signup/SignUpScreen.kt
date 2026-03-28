package br.me.vitorcsouza.train.ui.presentation.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.me.vitorcsouza.train.ui.components.ButtonDefaultCustom
import br.me.vitorcsouza.train.ui.components.TextButtomCustom
import br.me.vitorcsouza.train.ui.components.TextFieldCustom
import br.me.vitorcsouza.train.ui.presentation.signup.components.SignUpHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit
) {
    val state = viewModel.state
    
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
        }
    }

    SignUpScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}

@Composable
fun SignUpScreenContent(
    state: SignUpStates,
    onEvent: (SignUpEvents) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(stiffness = 300f, dampingRatio = 0.8f)
        )
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                TextButtomCustom(
                    onClick = onNavigateToLogin,
                    text = "Already have an account? Sign In"
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                SignUpHeader()

                TextFieldCustom(
                    icon = Icons.Outlined.Person,
                    value = state.username,
                    onValueChange = { onEvent(SignUpEvents.OnUsernameChanged(it)) },
                    placeholder = "Username",
                    isPassword = false
                )

                TextFieldCustom(
                    icon = Icons.Outlined.Email,
                    value = state.email,
                    onValueChange = { onEvent(SignUpEvents.OnEmailChanged(it)) },
                    placeholder = "Email",
                    isPassword = false
                )

                TextFieldCustom(
                    icon = Icons.Outlined.Lock,
                    value = state.password,
                    onValueChange = { onEvent(SignUpEvents.OnPasswordChanged(it)) },
                    placeholder = "Password",
                    isPassword = true
                )

                TextFieldCustom(
                    icon = Icons.Outlined.Lock,
                    value = state.confirmPassword,
                    onValueChange = { onEvent(SignUpEvents.OnConfirmPasswordChanged(it)) },
                    placeholder = "Confirm Password",
                    isPassword = true
                )

                ButtonDefaultCustom(
                    onClick = { onEvent(SignUpEvents.OnSignUpClick) },
                    text = "Sign Up"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreenContent(
        state = SignUpStates(),
        onEvent = {},
        onNavigateToLogin = {}
    )
}
