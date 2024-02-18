package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

sealed interface RateUserUiState {
    data object Idle: RateUserUiState
    data object OnSet: RateUserUiState
    data class OnError(val error: String): RateUserUiState
}