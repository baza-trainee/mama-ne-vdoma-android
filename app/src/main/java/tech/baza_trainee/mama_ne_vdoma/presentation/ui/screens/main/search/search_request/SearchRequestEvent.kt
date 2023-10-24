package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request

sealed interface SearchRequestEvent {
    data object OnBack: SearchRequestEvent
    data object OnMain: SearchRequestEvent
    data object SearchUser: SearchRequestEvent
    data object SearchGroup: SearchRequestEvent
    data object ResetUiState : SearchRequestEvent
    data class ValidateEmail(val value: String) : SearchRequestEvent
}