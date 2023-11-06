package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ChildInfoGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ObserveAsEvents
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun ChildInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<ChildInfoViewState> = mutableStateOf(ChildInfoViewState()),
    events: Flow<RequestState> = flowOf(),
    handleEvent: (ChildInfoEvent) -> Unit = { _ -> }
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BackHandler { handleEvent(ChildInfoEvent.OnBack) }

        val context = LocalContext.current

        ObserveAsEvents(events) {
            when (it) {
                RequestState.Idle -> Unit
                is RequestState.OnError -> context.showToast(it.error)
            }
        }

        ChildInfoGroup(
            modifier = Modifier.fillMaxWidth(),
            screenState = screenState,
            handleEvent = handleEvent
        )
    }
}

@Composable
@Preview
fun ChildInfoPreview() {
    ChildInfoScreen()
}
