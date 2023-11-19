package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun ChildScheduleScreen(
    screenState: ScheduleViewState,
    uiState: State<RequestState>,
    handleEvent: (ScheduleEvent) -> Unit
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
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = screenState,
        onUpdateSchedule = { day, period -> handleEvent(ScheduleEvent.UpdateChildSchedule(day, period)) },
        onUpdateComment = { handleEvent(ScheduleEvent.UpdateChildComment(it)) },
        onNext = { handleEvent(ScheduleEvent.PatchChildSchedule) },
        onBack = { handleEvent(ScheduleEvent.OnBack) }
    )
}

@Composable
@Preview
fun ChildScheduleScreenPreview() {
    ChildScheduleScreen(
        screenState = ScheduleViewState(),
        uiState = remember {
            mutableStateOf(RequestState.Idle)
        },
        handleEvent = {}
    )
}
