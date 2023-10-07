package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.createUserNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.loginNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.startNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.userProfileGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Mama_ne_vdomaTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "start_graph"
                ) {
                    startNavGraph(navController = navController)
                    loginNavGraph(navController = navController)
                    createUserNavGraph(navController = navController)
                    userProfileGraph(navController = navController)
                }
            }
        }
    }
}