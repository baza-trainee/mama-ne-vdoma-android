package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class LoginViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
