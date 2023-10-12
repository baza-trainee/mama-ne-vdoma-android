package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

sealed interface RestorePasswordEvent {
    data object OnBack: RestorePasswordEvent
    data class OnLogin(val email: String): RestorePasswordEvent
    data object SendEmail: RestorePasswordEvent
    data object ResetUiState: RestorePasswordEvent
    data class ValidateEmail(val email: String) : RestorePasswordEvent
}