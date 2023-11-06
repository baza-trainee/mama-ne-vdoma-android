package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.GroupInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundGroupScreen(
    modifier: Modifier = Modifier,
    screenState: State<FoundGroupViewState> = mutableStateOf(FoundGroupViewState()),
    uiState: State<FoundGroupUiState> = mutableStateOf(FoundGroupUiState.Idle),
    handleEvent: (FoundGroupEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
        BackHandler { handleEvent(FoundGroupEvent.OnBack) }

        val context = LocalContext.current

        var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

        when (val state = uiState.value) {
            FoundGroupUiState.Idle -> Unit
            is FoundGroupUiState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(FoundGroupEvent.ResetUiState)
            }

            FoundGroupUiState.OnRequestSent -> showSuccessDialog = true
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Пошук групи",
                avatar = screenState.value.avatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(FoundGroupEvent.OnAvatarClicked) },
                onBack = { handleEvent(FoundGroupEvent.OnBack) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (screenState.value.groups.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = "Рекомендовані групи",
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = 16.sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { handleEvent(FoundGroupEvent.GoToMain) },
                        text = "Створити групу",
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    itemsIndexed(screenState.value.groups) { index, group ->
                        if (index != 0)
                            Spacer(modifier = Modifier.height(8.dp))

                        GroupInfoDesk(
                            modifier = Modifier.fillMaxWidth(),
                            group = group,
                            currentUserId = screenState.value.currentUserId,
                            onSelect = { handleEvent(FoundGroupEvent.OnSelect(it)) }
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = { handleEvent(FoundGroupEvent.OnJoin) },
                    enabled = screenState.value.groups.map { it.isChecked }.contains(true)
                ) {
                    ButtonText(
                        text = "Приєднатися до групи"
                    )
                }
            } else if (!screenState.value.isLoading) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 16.dp)
                        .align(alignment = Alignment.End)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { handleEvent(FoundGroupEvent.GoToMain) },
                    text = "Створити групу",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = "На жаль, групи за даною локацією не знайдені. Ви можете бути першою і створити нову групу",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = { handleEvent(FoundGroupEvent.GoToMain) }
                ) {
                    ButtonText(
                        text = "На головну"
                    )
                }
            }
        }

        if (showSuccessDialog) {
            AlertDialog(onDismissRequest = { showSuccessDialog = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ok),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = "Запит до групи успішно відправлений. Ми повідомимо вас, коли адміністратор групи затвердить ваш запит",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = redHatDisplayFontFamily
                    )

                    Text(
                        text = "На головну",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.End)
                            .clickable {
                                showSuccessDialog = false
                                handleEvent(FoundGroupEvent.GoToMain)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun FoundGroupScreenPreview() {
    FoundGroupScreen()
}