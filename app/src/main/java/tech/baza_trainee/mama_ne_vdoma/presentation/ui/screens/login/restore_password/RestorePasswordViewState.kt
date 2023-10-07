package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class RestorePasswordViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
