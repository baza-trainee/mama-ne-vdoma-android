package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import androidx.compose.runtime.Stable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel

data class NotificationsViewState(
    @Stable val myJoinRequests: List<JoinRequestUiModel> = emptyList(),
    @Stable val adminJoinRequests: List<JoinRequestUiModel> = emptyList(),
    val isLoading: Boolean = false
)