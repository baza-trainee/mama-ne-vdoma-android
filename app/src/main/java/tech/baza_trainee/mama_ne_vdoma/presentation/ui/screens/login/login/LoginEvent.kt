package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

sealed interface LoginEvent {
    object OnSuccessfulLogin: LoginEvent
    object ConsumeRequestError: LoginEvent
    object LoginUser: LoginEvent
    data class ValidateEmail(val email: String) : LoginEvent
    data class ValidatePassword(val password: String) : LoginEvent
}