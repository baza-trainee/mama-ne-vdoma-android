package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

sealed interface VerifyEmailEvent {
    object OnBack: VerifyEmailEvent
    object ResendCode: VerifyEmailEvent
    object ResetUiState: VerifyEmailEvent
    data class VerifyEmail(val otp: String, val otpInputFilled: Boolean) : VerifyEmailEvent
}