package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import java.time.DayOfWeek

@Composable
@Preview
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    title: String = "Title",
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    isCommentNeeded: Boolean = true,
    comment: State<String> = mutableStateOf(""),
    onUpdateSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onUpdateComment: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        BackHandler { onBack() }

        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            HeaderWithOptArrow(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = title,
                onBack = onBack
            )

            ConstraintLayout(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .constrainAs(content) {
                        top.linkTo(topGuideline, 24.dp)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    }
            ) {
                val (schedule, commentField) = createRefs()

                ScheduleGroup(
                    modifier = modifier
                        .constrainAs(schedule) {
                            top.linkTo(parent.top)
                        },
                    scheduleModel = screenState.value.schedule,
                    onValueChange = { day, period -> onUpdateSchedule(day, period) }
                )

                if (isCommentNeeded)
                    OutlinedTextField(
                        modifier = modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                            .constrainAs(commentField) {
                                top.linkTo(schedule.bottom, 16.dp)
                                bottom.linkTo(parent.bottom, 16.dp)
                            },
                        value = comment.value,
                        label = { Text("Нотатка") },
                        onValueChange = { onUpdateComment(it) },
                        minLines = 3,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        ),
                        textStyle = TextStyle(
                            fontFamily = redHatDisplayFontFamily
                        )
                    )
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
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