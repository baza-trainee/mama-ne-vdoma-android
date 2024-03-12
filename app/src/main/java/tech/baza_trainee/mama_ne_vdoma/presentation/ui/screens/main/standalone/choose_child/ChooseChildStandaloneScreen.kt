package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun ChooseChildStandaloneScreen(
    isForSearch: Boolean,
    screenState: ChooseChildViewState,
    uiState: RequestState,
    handleEvent: (ChooseChildEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = if (isForSearch)
                    stringResource(id = R.string.title_group_search)
                else
                    stringResource(id = R.string.title_create_group),
                avatar = screenState.avatar,
                showNotification = true,
                notificationCount = screenState.notifications,
                onNotificationsClicked = { handleEvent(ChooseChildEvent.GoToNotifications) },
                onAvatarClicked = { handleEvent(ChooseChildEvent.OnAvatarClicked) },
                onBack = { handleEvent(ChooseChildEvent.OnBack) }
            )
        }
    ) { paddingValues ->
        BackHandler { handleEvent(ChooseChildEvent.OnBack) }

        val context = LocalContext.current

        when(uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(ChooseChildEvent.ResetUiState)
            }
        }

        var selectedChild: ChildEntity? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = size_16_dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                text = if (isForSearch)
                    stringResource(id = R.string.search_group_for)
                else
                    stringResource(id = R.string.create_group_for),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_18_sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = size_16_dp),
                verticalArrangement = Arrangement.spacedBy(size_8_dp)
            ) {
                items(screenState.children) { child ->
                    ChildCard(
                        modifier = Modifier.fillMaxWidth(),
                        child = child,
                        isSelected = selectedChild == child,
                        onSelected = { selectedChild = it }
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(vertical = size_16_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(ChooseChildEvent.OnChooseChild(selectedChild?.childId.orEmpty())) },
                enabled = selectedChild != null
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_next)
                )
            }
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ChooseChildScreenPreview() {
    ChooseChildStandaloneScreen(
        isForSearch = true,
        screenState = ChooseChildViewState(),
        uiState = RequestState.Idle,
        handleEvent = {}
    )
}