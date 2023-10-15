package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

sealed interface FoundGroupEvent {
    data object ResetUiState: FoundGroupEvent
    data object OnBack: FoundGroupEvent
}