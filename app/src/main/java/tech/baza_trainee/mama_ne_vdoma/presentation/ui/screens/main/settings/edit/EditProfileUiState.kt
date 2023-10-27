package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

sealed interface EditProfileUiState {
    data object Idle: EditProfileUiState
    data object OnAvatarError: EditProfileUiState
    data object OnProfileSaved: EditProfileUiState
    data class OnError(val error: String): EditProfileUiState
}