package br.me.vitorcsouza.train.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.me.vitorcsouza.train.ui.presentation.edit_workout.EditWorkoutScreen
import br.me.vitorcsouza.train.ui.presentation.home.HomeScreen
import br.me.vitorcsouza.train.ui.presentation.login.LoginScreen
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpScreen
import br.me.vitorcsouza.train.ui.presentation.weekly_plan.WeeklyPlanScreen
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object HomeScreen : Screen("home_screen")
    object EditWorkoutScreen : Screen("edit_workout_screen")
    object WeeklyPlanScreen : Screen("weekly_plan_screen")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val startDestination = if (currentUser != null) {
        "${Screen.HomeScreen.route}/${currentUser.uid}"
    } else {
        Screen.LoginScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
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
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    navController.navigate("${Screen.HomeScreen.route}/$uid") {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.HomeScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(
                userId = userId,
                onClick = { navController.navigate(Screen.EditWorkoutScreen.route) },
                onClickDate = { navController.navigate("${Screen.WeeklyPlanScreen.route}/$userId") }
            )
        }

        composable(Screen.EditWorkoutScreen.route) {
            EditWorkoutScreen(
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screen.WeeklyPlanScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            WeeklyPlanScreen(
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
