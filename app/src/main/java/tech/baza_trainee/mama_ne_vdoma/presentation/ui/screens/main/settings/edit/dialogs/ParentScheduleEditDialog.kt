package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
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
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp)
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
                        .padding(horizontal = 16.dp)
                        .weight(0.8f),
                    text = "Внесіть зміни до свого графіку, коли можете доглядати дітей",
                    fontSize = 16.sp
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
                        top.linkTo(title.bottom, 8.dp)
                        bottom.linkTo(buttons.top, 8.dp)
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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    value = tempNote,
                    label = "Нотатка",
                    hint = "Введіть будь-які відомості, які Ви вважаєте важливими/корисними для інших користувачів",
                    onValueChange = { tempNote = it },
                    minLines = 3,
                    maxLines = 3,
                    isError = tempNote.length > 1000
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
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.6f),
                    onClick = {
                        val map = mutableStateMapOf<DayOfWeek, DayPeriod>().apply { putAll(tempSchedule) }
                        onSave(map, tempNote)
                        onDismissRequest()
                    }
                ) {
                    ButtonText(text = "Зберегти зміни")
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.4f),
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    ButtonText(text = "Закрити")
                }
            }
        }
    }
}