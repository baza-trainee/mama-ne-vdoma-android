package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

sealed interface ChooseChildEvent {
    data object OnBack: ChooseChildEvent
    data object ResetUiState : ChooseChildEvent
    data object OnAvatarClicked : ChooseChildEvent
    data object GoToNotifications : ChooseChildEvent

    data class OnChooseChild(val childId: String): ChooseChildEvent
}