package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
@Preview
fun ScheduleGroup(
    modifier: Modifier = Modifier,
    scheduleModel: ScheduleModel = ScheduleModel(),
    onValueChange: (DayOfWeek, Period) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(4.dp)),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(128.dp),
                text = Period.WHOLE_DAY.period,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = Period.MORNING.period,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = Period.NOON.period,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = Period.AFTERNOON.period,
                textAlign = TextAlign.Center
            )
        }
        DayOfWeek.values().sortedBy { it.value }.forEach { day ->
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(48.dp)
                        .width(128.dp)
                        .background(
                            color = if (scheduleModel.schedule[day]?.wholeDay == true) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            onValueChange(day, Period.WHOLE_DAY)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayName,
                        textAlign = TextAlign.Center,
                        color = if (scheduleModel.schedule[day]?.wholeDay == true) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = scheduleModel.schedule[day]?.morning == true,
                        onCheckedChange = {
                            onValueChange(day, Period.MORNING)
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = scheduleModel.schedule[day]?.noon == true,
                        onCheckedChange = {
                            onValueChange(day, Period.NOON)
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = scheduleModel.schedule[day]?.afternoon == true,
                        onCheckedChange = {
                            onValueChange(day, Period.AFTERNOON)
                        }
                    )
                }
            }
        }
    }
}