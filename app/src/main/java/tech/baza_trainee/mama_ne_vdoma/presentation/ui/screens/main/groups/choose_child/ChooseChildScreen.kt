package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun ChooseChildScreen(
    modifier: Modifier = Modifier,
    screenState: State<ChooseChildViewState> = mutableStateOf(ChooseChildViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (ChooseChildEvent) -> Unit = {}
) {
    BackHandler { handleEvent(ChooseChildEvent.OnBack) }

    val context = LocalContext.current

    when(val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(ChooseChildEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        var selectedChild: ChildEntity? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Для кого створюємо групу?",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            screenState.value.children.forEach { childEntity ->
                Spacer(modifier = Modifier.height(8.dp))

                ChildCard(
                    modifier = Modifier.fillMaxWidth(),
                    child = childEntity,
                    isSelected = selectedChild == childEntity,
                    onSelected = { selectedChild = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = { handleEvent(ChooseChildEvent.OnChooseChild(selectedChild?.childId.orEmpty())) },
            enabled = selectedChild != null
        ) {
            ButtonText(
                text = "Далі"
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ChooseChildScreenPreview() {
    ChooseChildScreen()
}