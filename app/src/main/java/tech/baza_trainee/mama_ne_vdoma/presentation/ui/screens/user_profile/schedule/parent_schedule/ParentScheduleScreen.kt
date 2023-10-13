package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun ParentScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (ScheduleEvent) -> Unit = {}
) {
    val context = LocalContext.current

    when(val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
            handleEvent(ScheduleEvent.ResetUiState)
        }
    }

    ScheduleScreen(
        title = "Визначіть свій графік, коли можете доглядати дітей",
        screenState = screenState,
        isCommentNeeded = false,
        onUpdateSchedule = { day, period -> handleEvent(ScheduleEvent.UpdateParentSchedule(day, period)) },
        onUpdateComment = {},
        onNext = { handleEvent(ScheduleEvent.PatchParentSchedule) },
        onBack = { handleEvent(ScheduleEvent.OnBack) }
    )

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun ParentScheduleScreenPreview() {
    ParentScheduleScreen()
}