package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRequestScreen(
    modifier: Modifier = Modifier,
    screenState: State<SearchRequestViewState> = mutableStateOf(SearchRequestViewState()),
    uiState: State<SearchRequestUiState> = mutableStateOf(SearchRequestUiState.Idle),
    handleEvent: (SearchRequestEvent) -> Unit = {}
) {
    BackHandler { handleEvent(SearchRequestEvent.OnBack) }

    var nothingFound by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        SearchRequestUiState.Idle -> Unit
        is SearchRequestUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(SearchRequestEvent.ResetUiState)
        }

        SearchRequestUiState.OnNothingFound -> {
            handleEvent(SearchRequestEvent.ResetUiState)
            nothingFound = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Знайти користувача",
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.value.email,
            label = "Email користувача",
            onValueChange = { handleEvent(SearchRequestEvent.ValidateEmail(it)) },
            isError = screenState.value.emailValid == ValidField.INVALID,
            errorText = "Ви ввели некоректний email",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { handleEvent(SearchRequestEvent.SearchUser) }
            )
        )

        Spacer(modifier = modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchRequestEvent.SearchUser)
            },
            enabled = screenState.value.emailValid == ValidField.VALID
        ) {
            ButtonText(
                text = "Розпочати пошук"
            )
        }

        Spacer(modifier = modifier.height(32.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = "чи",
                fontSize = 14.sp,
            )
            Box(
                modifier = modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
        }

        Spacer(modifier = modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchRequestEvent.SearchGroup)
            }
        ) {
            ButtonText(
                text = "Шукати групу за локацією"
            )
        }

        if (nothingFound) {
            AlertDialog(onDismissRequest = { nothingFound = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Користувача з заданими параметрами не знайдено",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    nothingFound = false
                                },
                            text = "Новий пошук",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    nothingFound = false
                                    handleEvent(SearchRequestEvent.OnMain)
                                },
                            text = "На головну",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun SearchRequestScreenPreview() {
    SearchRequestScreen()
}