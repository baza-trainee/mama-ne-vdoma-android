package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

sealed interface FullInfoEvent {
    object OnBack: FullInfoEvent
    object OnNext: FullInfoEvent
    object EditUser: FullInfoEvent
    object DeleteUser: FullInfoEvent
    object AddChild: FullInfoEvent
    object ResetUiState: FullInfoEvent
    data class EditChild(val id: String) : FullInfoEvent
    data class DeleteChild(val id: String) : FullInfoEvent
}