package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun ChildScheduleScreen(
    screenState: ScheduleViewState,
    uiState: RequestState,
    handleEvent: (ScheduleEvent) -> Unit
) {
    val context = LocalContext.current

    when(uiState) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(ScheduleEvent.ResetUiState)
        }
    }

    ScheduleScreen(
        title = stringResource(id = R.string.title_add_child_schedule),
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
        uiState = RequestState.Idle,
        handleEvent = {}
    )
}
