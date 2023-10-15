package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results

sealed interface SearchResultsEvent {
    data object OnBack: SearchResultsEvent
    data object OnNewSearch: SearchResultsEvent
    data object ResetUiState : SearchResultsEvent
}