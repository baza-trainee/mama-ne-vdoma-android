package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

sealed interface MainEvent {
    data object OnBack: MainEvent
    data object ResetUiState: MainEvent
    data object Search: MainEvent
    data object CreateNewGroup: MainEvent
    data class SetSearchRequest(val value: String): MainEvent
    data class SetSearchOption(val value: Int): MainEvent
}