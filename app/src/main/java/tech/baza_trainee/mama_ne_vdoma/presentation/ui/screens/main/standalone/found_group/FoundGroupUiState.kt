package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

sealed interface FoundGroupUiState {
    data object Idle: FoundGroupUiState
    data object OnRequestSent: FoundGroupUiState
    data class OnError(val error: String): FoundGroupUiState
}