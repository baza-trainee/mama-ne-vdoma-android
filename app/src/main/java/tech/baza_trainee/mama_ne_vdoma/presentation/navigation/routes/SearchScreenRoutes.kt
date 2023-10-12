package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class SearchScreenRoutes(val route: String): CommonRoute(route) {
    data object Search: SearchScreenRoutes("search_screen")
}