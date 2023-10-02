package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

sealed interface ChildrenInfoEvent {
    object ResetChild: ChildrenInfoEvent
    data class SetChild(val id: String) : ChildrenInfoEvent
    data class DeleteChild(val id: String) : ChildrenInfoEvent
}