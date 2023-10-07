package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

sealed interface FullInfoUiState {
    object Idle: FullInfoUiState
    object OnNext: FullInfoUiState
    object OnDelete: FullInfoUiState
    data class OnError(val error: String): FullInfoUiState
}