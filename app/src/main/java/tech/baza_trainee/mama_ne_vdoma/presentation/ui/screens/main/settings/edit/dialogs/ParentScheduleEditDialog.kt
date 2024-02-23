package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import java.time.DayOfWeek

@Composable
@Preview
fun ParentScheduleEditDialog(
    schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    note: String = "Note",
    onSave: (SnapshotStateMap<DayOfWeek, DayPeriod>, String) -> Unit = {_,_->},
    onDismissRequest: () -> Unit = {}
) {
    var tempNote by rememberSaveable { mutableStateOf(note) }
    var tempSchedule by rememberSaveable {
        val map = mutableMapOf<DayOfWeek, DayPeriod>().also { map ->
        DayOfWeek.entries.forEach {
            map[it] = (schedule[it] ?: DayPeriod()).copy()
        }
    }
        mutableStateOf(map.toMap())
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(size_8_dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = size_8_dp)
                .fillMaxWidth()
        ) {

            val (title, content, buttons) = createRefs()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        height = Dimension.wrapContent
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = size_16_dp)
                        .weight(0.8f),
                    text = stringResource(id = R.string.change_your_schedule),
                    fontSize = font_size_16_sp
                )

                IconButton(
                    modifier = Modifier.weight(0.2f),
                    onClick = onDismissRequest
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "close"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .constrainAs(content) {
                        top.linkTo(title.bottom, size_8_dp)
                        bottom.linkTo(buttons.top, size_8_dp)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScheduleGroup(
                    modifier = Modifier.fillMaxWidth(),
                    schedule = tempSchedule,
                    onValueChange = { day, period ->
                        tempSchedule = tempSchedule.updateSchedule(day, period)
                    }
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_8_dp),
                    value = tempNote,
                    label = stringResource(id = R.string.note),
                    hint = stringResource(id = R.string.note_hint),
                    onValueChange = { tempNote = it },
                    minLines = 3,
                    maxLines = 3,
                    isError = tempNote.length > 1000
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttons) {
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier
                        .height(size_48_dp)
                        .padding(horizontal = size_8_dp)
                        .weight(0.6f),
                    onClick = {
                        val map = mutableStateMapOf<DayOfWeek, DayPeriod>().apply { putAll(tempSchedule) }
                        onSave(map, tempNote)
                        onDismissRequest()
                    }
                ) {
                    ButtonText(text = stringResource(id = R.string.action_save_changes))
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(size_48_dp)
                        .padding(horizontal = size_8_dp)
                        .weight(0.4f),
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = size_1_dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    ButtonText(text = stringResource(id = R.string.action_close))
                }
            }
        }
    }
}