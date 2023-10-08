package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class StartRoutes(val route: String): CommonRoute(route) {
    object Start : StartRoutes("start_screen")
    object Info : StartRoutes("info_screen")
}
