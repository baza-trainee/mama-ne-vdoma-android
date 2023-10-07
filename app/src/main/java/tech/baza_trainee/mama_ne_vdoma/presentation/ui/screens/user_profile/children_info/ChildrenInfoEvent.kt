package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info

sealed interface ChildrenInfoEvent {
    object ResetChild: ChildrenInfoEvent
    data class SetChild(val id: String) : ChildrenInfoEvent
    data class DeleteChild(val id: String) : ChildrenInfoEvent
}