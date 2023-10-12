package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

sealed interface MainEvent {
    object ResetUiState: MainEvent
    object Search: MainEvent
    object CreateNewGroup: MainEvent
    data class SetSearchRequest(val value: String): MainEvent
    data class SetSearchOption(val value: Int): MainEvent
}