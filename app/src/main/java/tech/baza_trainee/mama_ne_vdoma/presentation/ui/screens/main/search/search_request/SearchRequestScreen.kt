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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRequestScreen(
    screenState: SearchRequestViewState = SearchRequestViewState(),
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
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = stringResource(id = R.string.find_user),
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        OutlinedTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = screenState.email,
            label = stringResource(id = R.string.user_email),
            hint = stringResource(id = R.string.email),
            onValueChange = { handleEvent(SearchRequestEvent.ValidateEmail(it)) },
            isError = screenState.emailValid == ValidField.INVALID,
            errorText = stringResource(id = R.string.incorrect_email),
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 48.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchRequestEvent.SearchUser)
            },
            enabled = screenState.emailValid == ValidField.VALID
        ) {
            ButtonText(
                text = stringResource(id = R.string.start_search)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(id = R.string.or),
                fontSize = 14.sp,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 48.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchRequestEvent.SearchGroup)
            }
        ) {
            ButtonText(
                text = stringResource(id = R.string.search_group_by_location)
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

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.user_not_found),
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp, top = 24.dp),
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
                            text = stringResource(id = R.string.action_search),
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
                            text = stringResource(id = R.string.action_go_to_main),
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

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun SearchRequestScreenPreview() {
    SearchRequestScreen(
        screenState = SearchRequestViewState(),
        uiState = remember { mutableStateOf(SearchRequestUiState.Idle) },
        handleEvent = {}
    )
}