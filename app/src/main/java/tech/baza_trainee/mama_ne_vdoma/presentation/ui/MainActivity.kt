package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.createUserNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.firstGroupSearchNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.loginNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.hostNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.startNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.userProfileGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Mama_ne_vdomaTheme {
                val navController = rememberNavController()

                val navigator: ScreenNavigator by inject()

                NavigationEffects(
                    navigationChannel = navigator.navigationChannel,
                    navHostController = navController
                )

                NavHost(
                    navController = navController,
                    startDestination = Graphs.Start.route
                ) {
                    startNavGraph(navController)
                    loginNavGraph(navController)
                    createUserNavGraph()
                    userProfileGraph(navController)
                    firstGroupSearchNavGraph()
                    hostNavGraph()
                }
            }
        }
    }
}