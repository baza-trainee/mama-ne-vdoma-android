package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import java.time.DayOfWeek

@Composable
@Preview
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    title: String = "Title",
    screenState: State<ScheduleViewState> = mutableStateOf(ScheduleViewState()),
    comment: State<String> = mutableStateOf(""),
    onUpdateSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onUpdateComment: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { onBack() }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                ScheduleGroup(
                    modifier = Modifier
                        .fillMaxWidth(),
                    scheduleModel = screenState.value.schedule,
                    onValueChange = { day, period -> onUpdateSchedule(day, period) }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
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

                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.End),
                    text = "до 1000 символів",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 12.sp,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
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