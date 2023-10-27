package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email

sealed interface VerifyEmailUiState {
    data object Idle: VerifyEmailUiState
    data object OnPasswordChanged: VerifyEmailUiState
    data object OnEmailChanged: VerifyEmailUiState
    data class OnError(val error: String): VerifyEmailUiState
}