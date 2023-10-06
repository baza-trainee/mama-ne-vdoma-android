package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import java.time.DayOfWeek
import java.util.UUID

@Composable
@Preview
fun ChildInfoDesk(
    child: ChildEntity = ChildEntity(
        childId = UUID.randomUUID().toString(),
        name = "Іванко",
        age = "5",
        gender = Gender.BOY,
        schedule = ScheduleModel(
            mutableStateMapOf(
                DayOfWeek.MONDAY to DayPeriod(morning = true),
                DayOfWeek.TUESDAY to DayPeriod(wholeDay = true),
                DayOfWeek.WEDNESDAY to DayPeriod(noon = true),
                DayOfWeek.THURSDAY to DayPeriod(morning = true, afternoon = true),
                DayOfWeek.FRIDAY to DayPeriod(noon = true, afternoon = true),
                DayOfWeek.SATURDAY to DayPeriod(afternoon = true),
                DayOfWeek.SUNDAY to DayPeriod(wholeDay = true),
            )
        )
    ),
    onEdit: (String) -> Unit = {},
    onDelete: (String) -> Unit = {}
) {
    val localModifier = Modifier

    Column(
        modifier = localModifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = localModifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = if (child.gender == Gender.BOY) R.drawable.ic_boy else R.drawable.ic_girl),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = localModifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = child.name,
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format("%s р.", child.age),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
            IconButton(onClick = { onEdit(child.childId) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { onDelete(child.childId) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = localModifier.height(8.dp))

        val morning = child.schedule.schedule.filter { it.value.morning }.keys
        val noon = child.schedule.schedule.filter { it.value.noon }.keys
        val afternoon = child.schedule.schedule.filter { it.value.afternoon }.keys
        val wholeDay = child.schedule.schedule.filter { it.value.wholeDay }.keys

        val dayText = "Дні, коли потрібно доглянути дитину"
        val periodText = "Час, коли потрібно доглянути дитину"

        if (morning.isNotEmpty())
            DayScheduleRow(
                morning,
                Period.MORNING,
                dayText,
                periodText
            )
        if (noon.isNotEmpty())
            DayScheduleRow(
                noon,
                Period.NOON,
                dayText,
                periodText
            )
        if (afternoon.isNotEmpty())
            DayScheduleRow(
                afternoon,
                Period.AFTERNOON,
                dayText,
                periodText
            )
        if (wholeDay.isNotEmpty())
            DayScheduleRow(
                wholeDay,
                Period.WHOLE_DAY,
                dayText,
                periodText
            )
    }
}