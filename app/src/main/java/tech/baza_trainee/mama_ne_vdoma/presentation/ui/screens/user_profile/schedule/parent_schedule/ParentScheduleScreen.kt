package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import de.palm.composestateevents.EventEffect
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState

@Composable
fun ParentScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    comment: State<String> = mutableStateOf(""),
    onHandleScheduleEvent: (ScheduleEvent) -> Unit = {},
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    EventEffect(
        event = screenState.value.requestSuccess,
        onConsumed = {}
    ) { onNext() }

    EventEffect(
        event = screenState.value.requestError,
        onConsumed = { onHandleScheduleEvent(ScheduleEvent.ConsumeRequestError) }
    ) { if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

    ScheduleScreen(
        title = "Визначіть свій графік, коли можете доглядати дітей",
        screenState = screenState,
        comment = comment,
        onUpdateSchedule = { day, period -> onHandleScheduleEvent(ScheduleEvent.UpdateParentSchedule(day, period)) },
        onUpdateComment = { onHandleScheduleEvent(ScheduleEvent.UpdateParentComment(it)) },
        onNext = { onHandleScheduleEvent(ScheduleEvent.PatchParentSchedule) },
        onBack = onBack
    )

    if (screenState.value.isLoading) LoadingIndicator()
}