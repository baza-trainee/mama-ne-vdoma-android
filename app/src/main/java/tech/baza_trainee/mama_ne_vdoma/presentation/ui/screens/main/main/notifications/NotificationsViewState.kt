package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel

data class NotificationsViewState(
    val joinRequests: List<JoinRequestUiModel> = emptyList(),
    val isLoading: Boolean = false
)