package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

sealed interface NewPasswordEvent {
    object ConsumeRequestError: NewPasswordEvent
    object ConsumeRequestSuccess: NewPasswordEvent
    object ResetPassword: NewPasswordEvent
    data class ValidatePassword(val password: String) : NewPasswordEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : NewPasswordEvent
}