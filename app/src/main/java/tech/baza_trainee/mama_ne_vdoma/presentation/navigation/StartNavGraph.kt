package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StartRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.InfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.Start

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.Start.route,
        startDestination = StartRoutes.Start.route
    ) {
        composable(StartRoutes.Start.route) {
            Start(
                onStart = { navController.navigate(StartRoutes.Info.route) },
                onLogin = { navController.navigate(Graphs.Login.route) }
            )
        }
        composable(StartRoutes.Info.route) {
            InfoScreen { navController.navigate(Graphs.CreateUser.route) }
        }
    }
}