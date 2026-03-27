package br.me.vitorcsouza.train

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import br.me.vitorcsouza.train.ui.presentation.NavGraph
import br.me.vitorcsouza.train.ui.presentation.splash.SplashScreen
import br.me.vitorcsouza.train.ui.theme.TrainTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemUI()

        setContent {
            val navController = rememberNavController()

            TrainTheme(darkTheme = false) {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen()
                } else {
                    NavGraph(navController = navController)
                }

                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplash = false
                }
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
