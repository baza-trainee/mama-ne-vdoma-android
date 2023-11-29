package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common

sealed interface UpdateDetailsUiState {
    data object Idle: UpdateDetailsUiState
    data object OnAvatarError: UpdateDetailsUiState
    data object OnSaved: UpdateDetailsUiState
    data object AddressNotChecked: UpdateDetailsUiState
    data object AddressNotFound: UpdateDetailsUiState
    data class OnError(val error: String): UpdateDetailsUiState
}