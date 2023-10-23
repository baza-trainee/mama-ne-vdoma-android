package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

sealed interface SearchUserEvent {
    data object OnBack: SearchUserEvent
    data object OnMain: SearchUserEvent
    data object OnSearch: SearchUserEvent
    data object ResetUiState : SearchUserEvent
    data class ValidateName(val value: String) : SearchUserEvent
    data class ValidateEmail(val value: String) : SearchUserEvent
}