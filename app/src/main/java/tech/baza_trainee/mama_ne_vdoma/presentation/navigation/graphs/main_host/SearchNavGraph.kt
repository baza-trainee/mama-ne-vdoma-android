package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request.SearchRequestScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request.SearchRequestViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsViewModel

fun NavGraphBuilder.searchNavGraph() {
    navigation(
        route = Graphs.HostNested.Search.route,
        startDestination = SearchScreenRoutes.SearchUser.route
    ) {
        composable(SearchScreenRoutes.SearchUser.route) {
            val searchRequestViewModel: SearchRequestViewModel = koinNavViewModel()
            SearchRequestScreen(
                screenState = searchRequestViewModel.viewState.asStateWithLifecycle(),
                uiState = searchRequestViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { searchRequestViewModel.handleEvent(it) }
            )
        }
        composable(SearchScreenRoutes.SearchResults.route) {
            val searchResultsViewModel: SearchResultsViewModel = koinNavViewModel()
            SearchResultsScreen(
                screenState = searchResultsViewModel.viewState.asStateWithLifecycle(),
                uiState = searchResultsViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { searchResultsViewModel.handleEvent(it) }
            )
        }
    }
}