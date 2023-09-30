package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class ChildNameViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val age: String = "",
    val ageValid: ValidField = ValidField.EMPTY,
    val gender: Gender = Gender.NONE
)
