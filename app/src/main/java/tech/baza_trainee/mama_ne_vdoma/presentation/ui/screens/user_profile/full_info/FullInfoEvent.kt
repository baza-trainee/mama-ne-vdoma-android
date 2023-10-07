package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

sealed interface FullInfoEvent {
    object DeleteUser: FullInfoEvent
    object ResetChild: FullInfoEvent
    object ResetUiState: FullInfoEvent
    data class SetChild(val id: String) : FullInfoEvent
    data class DeleteChild(val id: String) : FullInfoEvent
}