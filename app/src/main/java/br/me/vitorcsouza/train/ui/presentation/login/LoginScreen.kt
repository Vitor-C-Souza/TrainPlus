package br.me.vitorcsouza.train.ui.presentation.login

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.me.vitorcsouza.train.ui.components.ButtonDefaultCustom
import br.me.vitorcsouza.train.ui.components.TextFieldCustom
import br.me.vitorcsouza.train.ui.presentation.login.components.CreateAccount
import br.me.vitorcsouza.train.ui.components.TextButtomCustom
import br.me.vitorcsouza.train.ui.presentation.login.components.LoginHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    LoginScreenContent(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
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
                CreateAccount(
                    onClick = {}
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                LoginHeader()

                TextFieldCustom(
                    icon = Icons.Outlined.Email,
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.OnEmailChanged(it)) },
                    placeholder = "Email",
                    isPassword = false
                )

                TextFieldCustom(
                    icon = Icons.Outlined.Lock,
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
                    placeholder = "Password",
                    isPassword = true
                )

                ButtonDefaultCustom(
                    onClick = { onEvent(LoginEvent.OnLoginClick) },
                    text = "Sign in"
                )


                TextButtomCustom(
                    onClick = {},
                    text = "Forgot Password?"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginState(),
        onEvent = {}
    )
}
