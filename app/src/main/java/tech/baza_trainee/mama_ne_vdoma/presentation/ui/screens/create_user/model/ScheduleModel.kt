package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class ScheduleModel(
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
        DayOfWeek.values().forEach {
            map[it] = DayPeriod()
        }
    }
)

data class DayPeriod(
    val morning: Boolean = false,
    val noon: Boolean = false,
    val afternoon: Boolean = false,
    val wholeDay: Boolean = false
)

enum class Period(val period: String) {
    MORNING("Ранок"), NOON("День"), AFTERNOON("Вечір"), WHOLE_DAY("Цілий день")
}
