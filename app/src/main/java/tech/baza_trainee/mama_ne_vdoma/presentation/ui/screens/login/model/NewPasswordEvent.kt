package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model

sealed interface NewPasswordEvent {
    object ConsumeRequestError: NewPasswordEvent
    object OnSuccess: NewPasswordEvent
    data class ValidatePassword(val password: String) : NewPasswordEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : NewPasswordEvent
}