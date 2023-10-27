package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

sealed interface VerifyEmailEvent {
    data object OnBack: VerifyEmailEvent
    data object ResendCode: VerifyEmailEvent
    data object GoToMain: VerifyEmailEvent
    data object ResetUiState: VerifyEmailEvent
    data class Verify(val otp: String, val otpInputFilled: Boolean) : VerifyEmailEvent
}