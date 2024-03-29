package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ChildInfoGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun ChildInfoScreen(
    screenState: ChildInfoViewState,
    uiState: RequestState,
    handleEvent: (ChildInfoEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.title_add_child_data),
                onBack = { handleEvent(ChildInfoEvent.OnBack) }
            )
        }
    ) { paddingValues ->

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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = size_16_dp),
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
