package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

sealed interface SearchUserUiState {
    data object Idle: SearchUserUiState
    data object OnNothingFound: SearchUserUiState
    data class OnError(val error: String): SearchUserUiState
}