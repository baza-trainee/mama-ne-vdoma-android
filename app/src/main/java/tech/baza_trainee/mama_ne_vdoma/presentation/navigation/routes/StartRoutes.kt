package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class StartRoutes(val route: String): CommonRoute(route) {
    data object Start : StartRoutes("start_screen")
    data object Info : StartRoutes("info_screen")
}
