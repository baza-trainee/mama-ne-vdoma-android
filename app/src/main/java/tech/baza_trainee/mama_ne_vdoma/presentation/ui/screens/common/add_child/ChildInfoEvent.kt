package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child

import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender

sealed interface ChildInfoEvent {
    data object OnBack: ChildInfoEvent
    data object SaveChild: ChildInfoEvent
    data object ResetUiState: ChildInfoEvent
    data class ValidateChildName(val name: String) : ChildInfoEvent
    data class SetGender(val gender: Gender) : ChildInfoEvent
    data class ValidateAge(val age: String) : ChildInfoEvent
}