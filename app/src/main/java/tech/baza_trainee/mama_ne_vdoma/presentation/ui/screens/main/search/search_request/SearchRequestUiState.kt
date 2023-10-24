package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request

sealed interface SearchRequestUiState {
    data object Idle: SearchRequestUiState
    data object OnNothingFound: SearchRequestUiState
    data class OnError(val error: String): SearchRequestUiState
}