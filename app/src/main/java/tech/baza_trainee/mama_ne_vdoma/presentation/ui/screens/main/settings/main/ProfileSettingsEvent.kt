package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main

sealed interface ProfileSettingsEvent {
    data object OnBack: ProfileSettingsEvent
    data object ResetUiState : ProfileSettingsEvent
    data object ToggleEmail : ProfileSettingsEvent
    data object LogOut : ProfileSettingsEvent
    data object EditProfile : ProfileSettingsEvent
}