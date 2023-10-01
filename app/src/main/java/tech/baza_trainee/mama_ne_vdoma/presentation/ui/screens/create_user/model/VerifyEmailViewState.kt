package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class VerifyEmailViewState(
    val otpValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
