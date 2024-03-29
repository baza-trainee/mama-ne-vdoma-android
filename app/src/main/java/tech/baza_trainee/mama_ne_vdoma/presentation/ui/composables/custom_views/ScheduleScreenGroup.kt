package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

@Composable
fun ScheduleScreenGroup(
    modifier: Modifier = Modifier,
    screenState: ScheduleViewState = ScheduleViewState(),
    onUpdateSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onUpdateComment: (String) -> Unit = {},
    onNext: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ScheduleGroup(
                modifier = Modifier.fillMaxWidth(),
                schedule = screenState.schedule,
                onValueChange = { day, period -> onUpdateSchedule(day, period) }
            )

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                value = screenState.comment,
                label = stringResource(id = R.string.note),
                hint = stringResource(id = R.string.note_hint),
                onValueChange = { onUpdateComment(it) },
                minLines = 3,
                maxLines = 3,
                errorText = stringResource(id = R.string.note_error),
                isError = screenState.commentValid == ValidField.INVALID
            )

            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End),
                text = stringResource(id = R.string.note_text_length),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_12_sp,
                color = GrayText
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(vertical = size_16_dp)
                .fillMaxWidth()
                .height(size_48_dp),
            onClick = onNext
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_next)
            )
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}