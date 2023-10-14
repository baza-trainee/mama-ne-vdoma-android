package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.GroupInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.RadioGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    screenState: State<MainViewState> = mutableStateOf(MainViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (MainEvent) -> Unit = {}
) {
    BackHandler { handleEvent(MainEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
            handleEvent(MainEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { handleEvent(MainEvent.CreateNewGroup) },
            text = "Створити групу",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        GroupInfoDesk()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Пошук користувача",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 16.sp
        )

        OutlinedTextField(
            value = screenState.value.searchRequest,
            onValueChange = { handleEvent(MainEvent.SetSearchRequest(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Пошук") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            ),
            leadingIcon = {
                IconButton(
                    onClick = { handleEvent(MainEvent.Search) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search_user"
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = { handleEvent(MainEvent.SetSearchRequest("")) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear"
                    )
                }
            }
        )

        val items = listOf(
            "за email адресою",
            "за нікнеймом/ім’ям",
            "за групою"
        )

        RadioGroup(
            modifier = Modifier.fillMaxWidth(),
            radioGroupOptions = items,
            getText = { it },
            selected = "",
            onSelectedChange = { handleEvent(MainEvent.SetSearchOption(items.indexOf(it))) }
        )
    }

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}