package tech.baza_trainee.mama_ne_vdoma.domain.model

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

fun ScheduleModel?.ifNullOrEmpty(creator: () -> ScheduleModel): ScheduleModel {
    return if (this != null && schedule.isNotEmpty()) this
    else creator()
}

data class DayPeriod(
    val morning: Boolean = false,
    val noon: Boolean = false,
    val afternoon: Boolean = false,
    val wholeDay: Boolean = false
) {

    fun isFilled() = morning || noon || afternoon || wholeDay
}

enum class Period(val period: String) {
    MORNING("Ранок"), NOON("Обід"), AFTERNOON("Вечір"), WHOLE_DAY("Цілий день")
}
