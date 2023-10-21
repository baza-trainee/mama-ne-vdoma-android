package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SEARCH_PAGE

sealed class SearchScreenRoutes(override val route: String): CommonHostRoute(route, SEARCH_PAGE, "Пошук") {
    data object SearchUser: SearchScreenRoutes("search_user_screen")
    data object SearchResults: SearchScreenRoutes("search_results_screen")
}