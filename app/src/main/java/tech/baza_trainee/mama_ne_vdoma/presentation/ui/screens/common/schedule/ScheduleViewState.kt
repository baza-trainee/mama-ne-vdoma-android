package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule

import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

data class ScheduleViewState(
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val comment: String = "",
    val commentValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
