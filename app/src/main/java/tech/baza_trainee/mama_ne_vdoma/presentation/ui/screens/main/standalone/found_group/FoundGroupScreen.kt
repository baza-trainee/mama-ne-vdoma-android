package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.GroupInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.GenericAlertDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun FoundGroupScreen(
    screenState: FoundGroupViewState = FoundGroupViewState(),
    uiState: State<FoundGroupUiState> = mutableStateOf(FoundGroupUiState.Idle),
    handleEvent: (FoundGroupEvent) -> Unit = { _ -> }
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.title_group_search),
                avatar = screenState.avatar,
                showNotification = true,
                notificationCount = screenState.notifications,
                onNotificationsClicked = { handleEvent(FoundGroupEvent.GoToNotifications) },
                onAvatarClicked = { handleEvent(FoundGroupEvent.OnAvatarClicked) },
                onBack = { handleEvent(FoundGroupEvent.OnBack) }
            )
        }
    ) { paddingValues ->

        BackHandler { handleEvent(FoundGroupEvent.OnBack) }

        val context = LocalContext.current

        var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
        var selectedGroupName by rememberSaveable { mutableStateOf("") }

        when (val state = uiState.value) {
            FoundGroupUiState.Idle -> Unit
            is FoundGroupUiState.OnError -> {
                context.showToast(state.error)
                handleEvent(FoundGroupEvent.ResetUiState)
            }

            FoundGroupUiState.OnRequestSent -> showSuccessDialog = true
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = size_16_dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(size_8_dp))

            if (screenState.groups.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.recommended_groups),
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = font_size_16_sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { handleEvent(FoundGroupEvent.CreateGroup) },
                        text = stringResource(id = R.string.action_create_group),
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = font_size_14_sp,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_8_dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(size_8_dp)
                ) {
                    items(screenState.groups) { group ->
                        GroupInfoDesk(
                            modifier = Modifier.fillMaxWidth(),
                            group = group,
                            currentUserId = screenState.currentUserId,
                            onSelect = {
                                selectedGroupName = group.name
                                handleEvent(FoundGroupEvent.OnSelect(it))
                            }
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(vertical = size_16_dp)
                        .fillMaxWidth()
                        .height(size_48_dp),
                    onClick = { handleEvent(FoundGroupEvent.OnJoin) },
                    enabled = screenState.groups.map { it.isChecked }.contains(true)
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.action_join)
                    )
                }
            } else if (!screenState.isLoading) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(alignment = Alignment.End)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { handleEvent(FoundGroupEvent.GoToMain) },
                    text = stringResource(id = R.string.action_create_group),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = font_size_14_sp,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_32_dp),
                    text = stringResource(id = R.string.groups_not_found),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = font_size_14_sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .padding(vertical = size_16_dp)
                        .fillMaxWidth()
                        .height(size_48_dp),
                    onClick = { handleEvent(FoundGroupEvent.GoToMain) }
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.action_go_to_main)
                    )
                }
            }
        }

        if (showSuccessDialog) {
            GenericAlertDialog(
                icon = painterResource(id = R.drawable.ic_ok),
                text = stringResource(id = R.string.join_request_sent, selectedGroupName),
                confirmButtonText = stringResource(id = R.string.action_go_to_main),
                confirmButtonAction = {
                    showSuccessDialog = false
                    handleEvent(FoundGroupEvent.GoToMain)
                },
                onDismissRequest = { showSuccessDialog = false }
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun FoundGroupScreenPreview() {
    FoundGroupScreen(
        screenState = FoundGroupViewState(),
        uiState = remember { mutableStateOf(FoundGroupUiState.Idle) },
        handleEvent = { _ -> }
    )
}