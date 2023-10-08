package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.CommonUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState

@Composable
fun ChildScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    uiState: State<CommonUiState> = mutableStateOf(CommonUiState.Idle),
    comment: State<String> = mutableStateOf(""),
    handleEvent: (ScheduleEvent) -> Unit = {}
) {
    val context = LocalContext.current

    when(val state = uiState.value) {
        CommonUiState.Idle -> Unit
        is CommonUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
            handleEvent(ScheduleEvent.ResetUiState)
        }
    }

    ScheduleScreen(
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = screenState,
        comment = comment,
        onUpdateSchedule = { day, period -> handleEvent(ScheduleEvent.UpdateChildSchedule(day, period)) },
        onUpdateComment = { handleEvent(ScheduleEvent.UpdateChildComment(it)) },
        onNext = { handleEvent(ScheduleEvent.PatchChildSchedule) },
        onBack = { handleEvent(ScheduleEvent.OnBack) }
    )

    if (screenState.value.isLoading) LoadingIndicator()
}
