package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common

sealed interface LocationUiState {
    data object Idle: LocationUiState
    data object AddressNotChecked: LocationUiState
    data object AddressNotFound: LocationUiState
    data class OnError(val error: String): LocationUiState
}