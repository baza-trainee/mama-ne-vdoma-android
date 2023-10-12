package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class HostScreenRoutes(val route: String): CommonRoute(route) {
    data object Host: HostScreenRoutes("host_screen")
}