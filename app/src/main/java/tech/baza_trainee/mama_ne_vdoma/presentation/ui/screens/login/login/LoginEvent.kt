package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

sealed interface LoginEvent {

    data object LoginUser: LoginEvent
    data object OnBack: LoginEvent
    data object OnRestore: LoginEvent
    data object OnCreate: LoginEvent
    data object ResetUiState: LoginEvent
    data class ValidateEmail(val email: String) : LoginEvent
    data class ValidatePassword(val password: String) : LoginEvent
}