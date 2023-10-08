package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

sealed interface VerifyEmailUiState {
    object Idle: VerifyEmailUiState
    data class OnError(val error: String): VerifyEmailUiState
}