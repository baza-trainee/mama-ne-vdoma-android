package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule

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
fun ParentScheduleScreen(
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    uiState: State<CommonUiState> = mutableStateOf(CommonUiState.Idle),
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