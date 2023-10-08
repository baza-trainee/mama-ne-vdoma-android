package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StartRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.InfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start.Start

fun NavGraphBuilder.startNavGraph(
    screenNavigator: ScreenNavigator?
) {
    navigation(
        route = Graphs.Start.route,
        startDestination = StartRoutes.Start.route
    ) {
        composable(StartRoutes.Start.route) {
            Start(
                onStart = { screenNavigator?.navigate(StartRoutes.Info) },
                onLogin = { screenNavigator?.navigate(Graphs.Login) }
            )
        }
        composable(StartRoutes.Info.route) {
            InfoScreen { screenNavigator?.navigate(Graphs.CreateUser) }
        }
    }
}