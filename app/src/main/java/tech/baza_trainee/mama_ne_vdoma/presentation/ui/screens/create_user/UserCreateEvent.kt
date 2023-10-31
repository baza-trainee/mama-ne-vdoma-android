package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

sealed interface UserCreateEvent {
    data object OnBack: UserCreateEvent
    data object OnLogin: UserCreateEvent
    data object RegisterUser: UserCreateEvent
    data object ResetUiState: UserCreateEvent
    data class OnGoogleLogin(val token: String) : UserCreateEvent
    data class ValidateEmail(val email: String) : UserCreateEvent
    data class ValidatePassword(val password: String) : UserCreateEvent
    data class ValidateConfirmPassword(val confirmPassword: String) : UserCreateEvent
    data class UpdatePolicyCheck(val isChecked: Boolean) : UserCreateEvent
}