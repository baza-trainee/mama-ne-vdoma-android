package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun ChooseChildStandaloneScreen(
    modifier: Modifier = Modifier,
    isForSearch: Boolean = true,
    screenState: State<ChooseChildViewState> = mutableStateOf(ChooseChildViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (ChooseChildEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { handleEvent(ChooseChildEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(ChooseChildEvent.ResetUiState)
            }
        }

        var selectedChild: ChildEntity? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = if (isForSearch) "Пошук групи" else "Створення групи",
                avatar = screenState.value.avatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(ChooseChildEvent.OnAvatarClicked) },
                onBack = { handleEvent(ChooseChildEvent.OnBack) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = if (isForSearch) "Для кого шукаємо групу?" else "Для кого створюємо групу?",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(all = 16.dp)
            ) {
                itemsIndexed(screenState.value.children) { index, child ->
                    if (index != 0)
                        Spacer(modifier = Modifier.height(8.dp))

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
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(ChooseChildEvent.OnChooseChild(selectedChild?.childId.orEmpty())) },
                enabled = selectedChild != null
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ChooseChildScreenPreview() {
    ChooseChildStandaloneScreen()
}