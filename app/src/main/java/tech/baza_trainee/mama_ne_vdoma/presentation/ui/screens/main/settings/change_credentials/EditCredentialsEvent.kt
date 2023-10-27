package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials

sealed interface EditCredentialsEvent {
    data object OnBack: EditCredentialsEvent
    data object VerifyEmail: EditCredentialsEvent
    data class ValidateEmail(val email: String) : EditCredentialsEvent
    data class ValidatePassword(val password: String) : EditCredentialsEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : EditCredentialsEvent
    data object ResetUiState: EditCredentialsEvent
    data object ResetPassword: EditCredentialsEvent
}