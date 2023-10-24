package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel

data class NotificationsViewState(
    val myJoinRequests: List<JoinRequestUiModel> = emptyList(),
    val adminJoinRequests: List<JoinRequestUiModel> = emptyList(),
    val isLoading: Boolean = false
)