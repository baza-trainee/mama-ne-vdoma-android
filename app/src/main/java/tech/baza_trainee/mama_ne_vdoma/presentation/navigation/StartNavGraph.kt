package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.InfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.Start

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "start_graph",
        startDestination = "start_screen"
    ) {
        composable("start_screen") {
            Start(
                onStart = { navController.navigate("info_screen") },
                onLogin = { navController.navigate("login_screen") }
            )
        }
        composable("info_screen") {
            InfoScreen { navController.navigate("create_user_graph") }
        }
    }
}