package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ChildInfoGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun ChildInfoScreen(
    modifier: Modifier = Modifier,
    screenState: ChildInfoViewState,
    uiState: RequestState,
    handleEvent: (ChildInfoEvent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        BackHandler { handleEvent(ChildInfoEvent.OnBack) }

        val context = LocalContext.current

        when (uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(ChildInfoEvent.ResetUiState)
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
    ChildInfoScreen(
        screenState = ChildInfoViewState(),
        uiState = RequestState.Idle,
        handleEvent = { _ -> }
    )
}