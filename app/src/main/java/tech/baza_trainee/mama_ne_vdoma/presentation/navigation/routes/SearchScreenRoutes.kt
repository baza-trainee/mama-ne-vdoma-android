package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class SearchScreenRoutes(val route: String): CommonRoute(route) {
    data object SearchUser: SearchScreenRoutes("search_user_screen")
    data object SearchResults: SearchScreenRoutes("search_results_screen")
}