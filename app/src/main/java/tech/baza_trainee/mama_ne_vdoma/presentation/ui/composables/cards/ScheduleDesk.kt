package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.DayScheduleRow
import java.time.DayOfWeek

@Composable
fun ScheduleInfoDesk(
    modifier: Modifier = Modifier,
    schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
    dayText: String,
    periodText: String
) {
    Column(
        modifier = modifier
    ) {
        val morning = schedule.filter { it.value.morning }.keys
        val noon = schedule.filter { it.value.noon }.keys
        val afternoon = schedule.filter { it.value.afternoon }.keys
        val wholeDay = schedule.filter { it.value.wholeDay }.keys

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