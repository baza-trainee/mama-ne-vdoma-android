package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

sealed interface VerifyEmailEvent {
    object ConsumeRequestError: VerifyEmailEvent
    object ResendCode: VerifyEmailEvent
    data class VerifyEmail(val otp: String, val otpInputFilled: Boolean, val onSuccess: () -> Unit) : VerifyEmailEvent
}