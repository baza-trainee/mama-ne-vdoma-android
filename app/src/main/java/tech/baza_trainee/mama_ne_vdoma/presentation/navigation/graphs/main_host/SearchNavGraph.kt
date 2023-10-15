package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user.SearchUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user.SearchUserViewModel

fun NavGraphBuilder.searchNavGraph() {
    navigation(
        route = Graphs.Host.Search.route,
        startDestination = SearchScreenRoutes.SearchUser.route
    ) {
        composable(SearchScreenRoutes.SearchUser.route) {
            val searchUserViewModel: SearchUserViewModel = koinNavViewModel()
            SearchUserScreen(
                screenState = searchUserViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = searchUserViewModel.uiState,
                handleEvent = { searchUserViewModel.handleEvent(it) }
            )
        }
        composable(SearchScreenRoutes.SearchResults.route) {
            val searchResultsViewModel: SearchResultsViewModel = koinNavViewModel()
            SearchResultsScreen(
                screenState = searchResultsViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = searchResultsViewModel.uiState,
                handleEvent = { searchResultsViewModel.handleEvent(it) }
            )
        }
    }
}