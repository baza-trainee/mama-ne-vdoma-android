package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class ChildInfoViewState(
    val nameValid: ValidField = ValidField.EMPTY,
    val ageValid: ValidField = ValidField.EMPTY,
    val gender: Gender = Gender.NONE
)
