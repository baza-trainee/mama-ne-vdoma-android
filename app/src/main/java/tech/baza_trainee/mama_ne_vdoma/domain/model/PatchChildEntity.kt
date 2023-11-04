package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class PatchChildEntity(
    val comment: String,
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule()
)
