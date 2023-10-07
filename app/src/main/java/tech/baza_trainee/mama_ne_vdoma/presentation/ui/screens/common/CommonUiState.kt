package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common

sealed interface CommonUiState {
    object Idle: CommonUiState
    object OnNext: CommonUiState
    data class OnError(val error: String): CommonUiState
}