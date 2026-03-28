package br.me.vitorcsouza.train.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.me.vitorcsouza.train.ui.presentation.home.HomeScreen
import br.me.vitorcsouza.train.ui.presentation.login.LoginScreen
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object HomeScreen : Screen("home_screen")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) },
                onLoginSuccess = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    navController.navigate("${Screen.HomeScreen.route}/$userId") {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
                )
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(onNavigateToLogin = { navController.popBackStack() })
        }

        composable(
            route = "${Screen.HomeScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(
                userId = userId,
                onClick = { /* Navegar para adicionar treino futuramente */ }
            )
        }

    }
}