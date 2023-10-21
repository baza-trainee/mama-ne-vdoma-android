package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE

sealed class MainScreenRoutes(override val route: String): CommonHostRoute(route, MAIN_PAGE, "Головна") {
    data object Main: MainScreenRoutes("main_screen")
}