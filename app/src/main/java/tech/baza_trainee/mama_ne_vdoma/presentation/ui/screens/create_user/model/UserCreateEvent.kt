package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

sealed interface UserCreateEvent {
    object ConsumeRegisterError: UserCreateEvent
    object RegisterUser: UserCreateEvent
    data class ValidateEmail(val email: String) : UserCreateEvent
    data class ValidatePassword(val password: String) : UserCreateEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : UserCreateEvent
    data class UpdatePolicyCheck(val isChecked: Boolean) : UserCreateEvent
}