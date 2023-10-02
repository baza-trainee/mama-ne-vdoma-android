package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender

sealed interface ChildInfoEvent {
    object SaveCurrentChild: ChildInfoEvent
    data class ValidateChildName(val name: String) : ChildInfoEvent
    data class SetGender(val gender: Gender) : ChildInfoEvent
    data class ValidateAge(val age: String) : ChildInfoEvent
}