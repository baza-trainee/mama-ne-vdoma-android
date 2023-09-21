package tech.baza_trainee.mama_ne_vdoma.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.ui.screens.start.InfoScreenFunc
import tech.baza_trainee.mama_ne_vdoma.ui.screens.start.StartScreenFunc

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "start_graph",
        startDestination = "start_screen"
    ) {
        composable("start_screen") {
            StartScreenFunc(
                onStart = { navController.navigate("info_screen") },
                onLogin = { navController.navigate("login_screen") }
            )
        }
        composable("info_screen") {
            InfoScreenFunc { navController.navigate("create_user_screen") }
        }
    }
}