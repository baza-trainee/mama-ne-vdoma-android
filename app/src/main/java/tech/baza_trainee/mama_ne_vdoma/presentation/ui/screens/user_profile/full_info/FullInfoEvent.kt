package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

sealed interface FullInfoEvent {
    data object OnBack: FullInfoEvent
    data object OnNext: FullInfoEvent
    data object EditUser: FullInfoEvent
    data object DeleteUser: FullInfoEvent
    data object AddChild: FullInfoEvent
    data object ResetUiState: FullInfoEvent
    data class EditChild(val id: String) : FullInfoEvent
    data class DeleteChild(val id: String) : FullInfoEvent
}