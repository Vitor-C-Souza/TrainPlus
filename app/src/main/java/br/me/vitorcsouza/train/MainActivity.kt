package br.me.vitorcsouza.train

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import br.me.vitorcsouza.train.ui.presentation.login.LoginScreen
import br.me.vitorcsouza.train.ui.presentation.splash.SplashScreen
import br.me.vitorcsouza.train.ui.theme.TrainTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen()
                } else {
                    LoginScreen()
                }

                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplash = false
                }
            }
        }
    }
}
