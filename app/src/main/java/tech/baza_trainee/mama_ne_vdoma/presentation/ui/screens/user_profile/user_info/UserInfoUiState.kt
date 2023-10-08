package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

sealed interface UserInfoUiState {
    object Idle: UserInfoUiState
    object OnAvatarError: UserInfoUiState
    data class OnError(val error: String): UserInfoUiState
}