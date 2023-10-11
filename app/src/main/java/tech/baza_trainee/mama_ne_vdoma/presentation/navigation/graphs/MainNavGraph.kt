package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.MainScreen

fun NavGraphBuilder.mainNavGraph(
    navHostController: NavHostController
) {
    navigation(
        route = Graphs.Main.route,
        startDestination = MainScreenRoutes.Main.route
    ) {
        composable(MainScreenRoutes.Main.route) {
            MainScreen()
        }
    }
}