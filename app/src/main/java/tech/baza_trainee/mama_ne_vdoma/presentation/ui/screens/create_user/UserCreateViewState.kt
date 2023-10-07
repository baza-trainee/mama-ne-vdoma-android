package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserCreateViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPassword: String = "",
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isPolicyChecked: Boolean = false,
    val isAllConform: Boolean = false,
    val isLoading: Boolean = false
)
