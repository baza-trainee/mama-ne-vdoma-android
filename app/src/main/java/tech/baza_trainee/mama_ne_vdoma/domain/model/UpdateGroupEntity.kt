package tech.baza_trainee.mama_ne_vdoma.domain.model

import java.time.DayOfWeek

data class UpdateGroupEntity(
    val name: String = "",
    val desc: String = "",
    val ages: String = "",
    val avatar: String = "",
    val schedule: Map<DayOfWeek, DayPeriod> = getDefaultSchedule()
)
