package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserCreateViewState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isPolicyChecked: Boolean = false,
    val isAllConform: Boolean = false
)
