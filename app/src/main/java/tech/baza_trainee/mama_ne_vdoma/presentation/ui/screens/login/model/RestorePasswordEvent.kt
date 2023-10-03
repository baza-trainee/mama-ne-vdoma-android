package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model

sealed interface RestorePasswordEvent {
    object ConsumeRequestError: RestorePasswordEvent
    object OnSuccess: RestorePasswordEvent
    data class ValidateEmail(val email: String) : RestorePasswordEvent
}