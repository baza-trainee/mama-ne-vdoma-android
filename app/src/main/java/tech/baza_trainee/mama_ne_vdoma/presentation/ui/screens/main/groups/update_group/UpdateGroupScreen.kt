package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsInputScreen

@Composable
fun UpdateGroupScreen(
    screenState: UpdateGroupViewState,
    uiState: UpdateDetailsUiState,
    handleEvent: (GroupDetailsEvent) -> Unit
) {
    BackHandler { handleEvent(GroupDetailsEvent.OnBack) }

    GroupDetailsInputScreen(
        screenState = screenState.groupDetails,
        uiState = uiState,
        isForEditing = true,
        handleEvent = handleEvent
    )

    if (screenState.isLoading) LoadingIndicator()
}