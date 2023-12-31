package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsInputScreen

@Composable
fun CreateGroupScreen(
    screenState: CreateGroupViewState,
    uiState: State<UpdateDetailsUiState>,
    handleEvent: (GroupDetailsEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Створення нової групи",
                avatar = screenState.groupDetails.userAvatar,
                showNotification = true,
                notificationCount = screenState.notifications,
                onNotificationsClicked = { handleEvent(GroupDetailsEvent.GoToNotifications) },
                onAvatarClicked = { handleEvent(GroupDetailsEvent.OnAvatarClicked) },
                onBack = { handleEvent(GroupDetailsEvent.OnBack) }
            )
        }
    ) { paddingValues ->

        BackHandler { handleEvent(GroupDetailsEvent.OnBack) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            GroupDetailsInputScreen(
                screenState = screenState.groupDetails,
                uiState = uiState,
                handleEvent = handleEvent
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CreateGroupScreenPreview() {
    CreateGroupScreen(
        screenState = CreateGroupViewState(),
        uiState = remember { mutableStateOf(UpdateDetailsUiState.Idle) },
        handleEvent = {}
    )
}