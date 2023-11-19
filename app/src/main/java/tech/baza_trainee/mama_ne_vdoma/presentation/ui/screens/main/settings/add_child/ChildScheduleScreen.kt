package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.add_child

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleScreenGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun ChildScheduleScreenInSettings(
    screenState: ScheduleViewState,
    uiState: State<RequestState>,
    handleEvent: (ScheduleEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BackHandler {
            handleEvent(ScheduleEvent.OnBack)
        }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                handleEvent(ScheduleEvent.ResetUiState)
            }
        }

        ScheduleScreenGroup(
            modifier = Modifier.fillMaxWidth(),
            screenState = screenState,
            onUpdateSchedule = { day, period -> handleEvent(ScheduleEvent.UpdateChildSchedule(day, period)) },
            onUpdateComment = { handleEvent(ScheduleEvent.UpdateChildComment(it)) },
            onNext = { handleEvent(ScheduleEvent.PatchChildSchedule) }
        )
    }
}

@Composable
@Preview
fun ChildScheduleScreenPreview() {
    ChildScheduleScreenInSettings(
        screenState = ScheduleViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}
