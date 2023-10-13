package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child

sealed interface ChooseChildEvent {
    data object OnBack: ChooseChildEvent
    data object ResetUiState : ChooseChildEvent

    data class OnChooseChild(val childId: String): ChooseChildEvent
}