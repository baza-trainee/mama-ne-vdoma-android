package tech.baza_trainee.mama_ne_vdoma.presentation.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.getDefaultSchedule
import java.time.DayOfWeek

data class ChildUiModel(
    val name: String = "",
    val age: String = "",
    val gender: Gender = Gender.NONE,
    val note: String = "",
    val parentId: String = "",
    val childId: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule()
)
