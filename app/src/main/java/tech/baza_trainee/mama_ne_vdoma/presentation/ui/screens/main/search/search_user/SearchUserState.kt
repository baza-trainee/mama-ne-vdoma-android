package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

sealed interface SearchUserState {
    data object Idle: SearchUserState
    data object OnNothingFound: SearchUserState
    data class OnError(val error: String): SearchUserState
}