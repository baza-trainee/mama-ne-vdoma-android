package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import androidx.compose.runtime.Stable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.NotificationsUiModel

@Stable
data class NotificationsViewState(
    val myJoinRequests: List<JoinRequestUiModel> = emptyList(),
    val adminJoinRequests: List<JoinRequestUiModel> = emptyList(),
    val notifications: List<NotificationsUiModel> = emptyList(),
    val isLoading: Boolean = false
)