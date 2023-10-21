package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule

import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import java.time.DayOfWeek

sealed interface ScheduleEvent {
    data object OnBack: ScheduleEvent
    data object PatchChildSchedule: ScheduleEvent
    data object PatchParentSchedule: ScheduleEvent
    data object ResetUiState: ScheduleEvent
    data class UpdateChildSchedule(val day: DayOfWeek, val period: Period) : ScheduleEvent
    data class UpdateChildComment(val comment: String): ScheduleEvent
    data class UpdateParentComment(val comment: String): ScheduleEvent
    data class UpdateParentSchedule(val day: DayOfWeek, val period: Period) : ScheduleEvent
}