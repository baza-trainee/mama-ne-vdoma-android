package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

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
fun ChildScheduleScreen(
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
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = screenState,
        comment = comment,
        onUpdateSchedule = { day, period -> onHandleScheduleEvent(ScheduleEvent.UpdateChildSchedule(day, period)) },
        onUpdateComment = { onHandleScheduleEvent(ScheduleEvent.UpdateChildComment(it)) },
        onNext = { onHandleScheduleEvent(ScheduleEvent.PatchChildSchedule) },
        onBack = onBack
    )

    if (screenState.value.isLoading) LoadingIndicator()
}
