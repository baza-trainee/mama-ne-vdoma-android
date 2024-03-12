package tech.baza_trainee.mama_ne_vdoma.domain.model

import java.time.DayOfWeek

data class PatchChildEntity(
    val comment: String,
    val schedule: Map<DayOfWeek, DayPeriod> = getDefaultSchedule()
)
