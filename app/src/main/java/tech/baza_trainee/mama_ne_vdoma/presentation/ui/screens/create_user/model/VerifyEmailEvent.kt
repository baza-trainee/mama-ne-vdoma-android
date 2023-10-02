package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

sealed interface VerifyEmailEvent {
    data class VerifyEmail(val otp: String, val otpInputFilled: Boolean, val onSuccess: () -> Unit) : VerifyEmailEvent
}