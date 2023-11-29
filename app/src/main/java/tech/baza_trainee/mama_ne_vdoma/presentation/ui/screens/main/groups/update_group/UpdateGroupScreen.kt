package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsInputScreen

@Composable
fun UpdateGroupScreen(
    screenState: UpdateGroupViewState,
    uiState: State<UpdateDetailsUiState>,
    handleEvent: (GroupDetailsEvent) -> Unit
) {
    BackHandler { handleEvent(GroupDetailsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        is UpdateDetailsUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        else -> Unit
    }

    GroupDetailsInputScreen(
        screenState = screenState.groupDetails,
        uiState = uiState,
        isForEditing = true,
        handleEvent = handleEvent
    )

    if (screenState.isLoading) LoadingIndicator()
}