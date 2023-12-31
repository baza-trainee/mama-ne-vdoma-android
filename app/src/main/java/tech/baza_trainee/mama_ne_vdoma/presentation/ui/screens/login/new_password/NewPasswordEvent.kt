package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

sealed interface NewPasswordEvent {
    data object OnBack: NewPasswordEvent
    data object ResetPassword: NewPasswordEvent
    data object ResetUiState: NewPasswordEvent
    data class ValidatePassword(val password: String) : NewPasswordEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : NewPasswordEvent
}