package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class NewPasswordViewState(
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPassword: String = "",
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
