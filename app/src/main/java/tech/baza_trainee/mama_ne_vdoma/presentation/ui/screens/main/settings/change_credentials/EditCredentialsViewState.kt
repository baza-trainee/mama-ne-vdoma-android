package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class EditCredentialsViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPassword: String = "",
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
