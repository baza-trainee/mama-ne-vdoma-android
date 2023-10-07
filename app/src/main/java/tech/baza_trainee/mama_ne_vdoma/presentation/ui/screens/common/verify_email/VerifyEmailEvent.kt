package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

sealed interface VerifyEmailEvent {
    object ConsumeLoginSuccess: VerifyEmailEvent
    object ConsumeRestoreSuccess: VerifyEmailEvent
    object ConsumeRequestError: VerifyEmailEvent
    object ResendCode: VerifyEmailEvent
    data class VerifyEmail(val otp: String, val otpInputFilled: Boolean) : VerifyEmailEvent
}