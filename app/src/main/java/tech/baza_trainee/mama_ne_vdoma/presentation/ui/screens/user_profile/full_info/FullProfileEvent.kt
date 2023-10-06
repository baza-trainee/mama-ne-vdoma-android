package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

sealed interface FullProfileEvent {
    object ConsumeRequestError: FullProfileEvent
    object UpdateFullProfile: FullProfileEvent
    object ResetChild: FullProfileEvent
    data class SetChild(val id: String) : FullProfileEvent
    data class DeleteChild(val id: String) : FullProfileEvent
}