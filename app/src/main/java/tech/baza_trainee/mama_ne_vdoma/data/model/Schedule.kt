package tech.baza_trainee.mama_ne_vdoma.data.model

data class WeekScheduleDto(
    val note: String,
    val week: Map<String, DayScheduleDto>
)

data class DayScheduleDto(
    val morning: Boolean,
    val lunch: Boolean,
    val evening: Boolean
)