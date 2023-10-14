package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group

sealed interface CreateGroupUiState {
    data object Idle: CreateGroupUiState
    data object OnAvatarError: CreateGroupUiState
    data class OnError(val error: String): CreateGroupUiState
}