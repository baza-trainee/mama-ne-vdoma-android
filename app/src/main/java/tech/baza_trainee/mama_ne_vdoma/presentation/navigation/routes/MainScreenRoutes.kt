package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class MainScreenRoutes(val route: String): CommonRoute(route) {
    data object Main: MainScreenRoutes("main_screen")
}