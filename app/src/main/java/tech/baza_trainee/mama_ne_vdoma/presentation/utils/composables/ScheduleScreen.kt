package tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ScheduleScreenState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import java.time.DayOfWeek

@Composable
@Preview
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    title: String = "Title",
    screenState: State<ScheduleScreenState> = mutableStateOf(ScheduleScreenState()),
    onUpdateSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onUpdateComment: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        val imeState = rememberImeState()
        val scrollState = rememberScrollState()
        val density = LocalDensity.current.density
        val offset = (5000 * density).toInt()

        LaunchedEffect(key1 = imeState.value) {
            if (imeState.value) {
                delay(100)
                scrollState.scrollTo(offset)
            }
        }

        ConstraintLayout(
            modifier = modifier
                .verticalScroll(scrollState)
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, schedule, comment, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            TopBarWithArrow(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = title,
                onBack = onBack
            )

            ChildScheduleGroup(
                modifier = modifier
                    .constrainAs(schedule) {
                        top.linkTo(topGuideline, 24.dp)
                    },
                scheduleModel = screenState.value.schedule,
                onValueChange = { day, period -> onUpdateSchedule(day, period) }
            )

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .constrainAs(comment) {
                        top.linkTo(schedule.bottom, 16.dp)
                        bottom.linkTo(btnNext.top, 16.dp)
                    },
                value = screenState.value.comment,
                label = { Text("Нотатка") },
                onValueChange = { onUpdateComment(it) },
                minLines = 3,
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor =  MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor =  MaterialTheme.colorScheme.surface,
                    disabledContainerColor =  MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                ),
                textStyle = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                )
            )

            Button(
                modifier = modifier
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = onNext
            ) {
                ButtonText(
                    text = "Встановити розклад"
                )
            }
        }
    }
}