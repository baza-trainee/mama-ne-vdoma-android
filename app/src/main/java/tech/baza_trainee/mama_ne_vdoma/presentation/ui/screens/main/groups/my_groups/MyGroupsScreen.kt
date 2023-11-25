package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.GroupInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun MyGroupsScreen(
    screenState: MyGroupsViewState,
    uiState: State<RequestState>,
    handleEvent: (MyGroupsEvent) -> Unit
) {
    BackHandler { handleEvent(MyGroupsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
            handleEvent(MyGroupsEvent.ResetUiState)
        }

        else -> Unit
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Мої групи",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleEvent(MyGroupsEvent.CreateNewGroup) },
                text = "Створити групу",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            itemsIndexed(screenState.groups) { index, group ->
                if (index != 0)
                    Spacer(modifier = Modifier.height(8.dp))

                GroupInfoDesk(
                    modifier = Modifier.fillMaxWidth(),
                    group = group,
                    currentUserId = screenState.userId,
                    onEdit = {  handleEvent(MyGroupsEvent.OnEdit(it)) },
                    onLeave = { handleEvent(MyGroupsEvent.OnLeave(it)) },
                    onSwitchAdmin = { groupId, memberId ->
                        handleEvent(
                            MyGroupsEvent.OnSwitchAdmin(
                                groupId,
                                memberId
                            )
                        )
                    },
                    onDelete = { handleEvent(MyGroupsEvent.OnDelete(it)) }
                )
            }
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun MyGroupsScreenPreview() {
    MyGroupsScreen(
        screenState = MyGroupsViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}