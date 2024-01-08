package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

sealed interface NotificationsUiState {
    data object Idle: NotificationsUiState
    data object OnAccepted: NotificationsUiState
    data class OnError(val error: String): NotificationsUiState
    data class GoToPage(val page: Int): NotificationsUiState
}