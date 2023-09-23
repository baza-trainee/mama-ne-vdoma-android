package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import java.time.DayOfWeek
import java.util.EnumMap

data class ChildScheduleModel(
    val schedule: EnumMap<DayOfWeek, DayPeriod> = EnumMap<DayOfWeek, DayPeriod>(DayOfWeek::class.java).also { map ->
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
