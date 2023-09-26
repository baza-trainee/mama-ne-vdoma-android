package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserInfoViewState(
    val userName: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val userPhone: String = "",
    val phoneValid: ValidField = ValidField.EMPTY
)
