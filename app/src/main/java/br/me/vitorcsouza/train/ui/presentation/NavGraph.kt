package br.me.vitorcsouza.train.ui.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.me.vitorcsouza.train.ui.presentation.login.LoginScreen
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpScreen

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object HomeScreen : Screen("home_screen")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) })
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(onNavigateToLogin = { navController.popBackStack() })
        }


    }
}