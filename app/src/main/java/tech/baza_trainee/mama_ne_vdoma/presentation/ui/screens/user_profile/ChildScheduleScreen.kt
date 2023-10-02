package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleViewState

@Composable
fun ChildScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    comment: State<String> = mutableStateOf(""),
    onHandleScheduleEvent: (ScheduleEvent) -> Unit = {},
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        onHandleScheduleEvent(ScheduleEvent.SetCurrentChildSchedule)
    }

    ScheduleScreen(
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = screenState,
        comment = comment,
        onUpdateSchedule = { day, period -> onHandleScheduleEvent(ScheduleEvent.UpdateChildSchedule(day, period)) },
        onUpdateComment = { onHandleScheduleEvent(ScheduleEvent.UpdateChildComment(it)) },
        onNext = onNext,
        onBack = onBack
    )
}
