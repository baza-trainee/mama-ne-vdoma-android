package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleViewState

@Composable
fun ParentScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    comment: State<String> = mutableStateOf(""),
    onHandleScheduleEvent: (ScheduleEvent) -> Unit = {},
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ScheduleScreen(
        title = "Визначіть свій графік, коли можете доглядати дітей",
        screenState = screenState,
        comment = comment,
        onUpdateSchedule = { day, period -> onHandleScheduleEvent(ScheduleEvent.UpdateParentSchedule(day, period)) },
        onUpdateComment = { onHandleScheduleEvent(ScheduleEvent.UpdateParentComment(it)) },
        onNext = onNext,
        onBack = onBack
    )
}