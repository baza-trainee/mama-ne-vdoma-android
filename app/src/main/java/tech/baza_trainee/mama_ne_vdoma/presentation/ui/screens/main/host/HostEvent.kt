package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

sealed interface HostEvent {
    data object OnBack: HostEvent
    data object OnBackLocal: HostEvent
    data object GoToNotifications : HostEvent
    data object ResetUiState : HostEvent
    data class SwitchTab(val index: Int): HostEvent
}