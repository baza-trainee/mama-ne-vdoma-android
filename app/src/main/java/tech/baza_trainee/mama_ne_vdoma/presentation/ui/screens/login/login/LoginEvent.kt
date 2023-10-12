package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

sealed interface LoginEvent {

    object LoginUser: LoginEvent
    object OnBack: LoginEvent
    object OnRestore: LoginEvent
    object OnCreate: LoginEvent
    object ResetUiState: LoginEvent
    data class ValidateEmail(val email: String) : LoginEvent
    data class ValidatePassword(val password: String) : LoginEvent
}