package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class UpdateGroupEntity(
    val name: String = "",
    val desc: String = "",
    val ages: String = "",
    val avatar: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule()
)
