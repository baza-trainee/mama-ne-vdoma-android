package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ParentScheduleEditDialog(
    modifier: Modifier = Modifier,
    scheduleModel: ScheduleModel = ScheduleModel(),
    note: String = "Note",
    noteValid: ValidField = ValidField.EMPTY,
    onEditSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onEditNote: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onRestore: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScheduleGroup(
                    modifier = Modifier.fillMaxWidth(),
                    scheduleModel = scheduleModel,
                    onValueChange = onEditSchedule
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    value = note,
                    label = "Нотатка",
                    onValueChange = onEditNote,
                    minLines = 3,
                    maxLines = 3,
                    isError = noteValid == ValidField.INVALID
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.6f),
                    onClick = {
                        onSave()
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
                    onClick = {
                        onRestore()
                        onDismissRequest()
                    },
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