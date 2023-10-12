package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

sealed interface HostEvent {
    object OnBack: HostEvent
    data class SwitchTab(val index: Int): HostEvent
}