package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class VerifyEmailViewState(
    val otp: String = "",
    val otpValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
