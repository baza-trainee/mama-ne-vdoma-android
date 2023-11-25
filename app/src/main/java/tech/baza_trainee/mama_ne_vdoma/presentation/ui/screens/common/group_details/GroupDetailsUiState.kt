package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details

sealed interface GroupDetailsUiState {
    data object Idle: GroupDetailsUiState
    data object OnAvatarError: GroupDetailsUiState
    data object OnGroupSaved: GroupDetailsUiState
    data object AddressNotChecked: GroupDetailsUiState
    data object AddressNotFound: GroupDetailsUiState
    data class OnError(val error: String): GroupDetailsUiState
}