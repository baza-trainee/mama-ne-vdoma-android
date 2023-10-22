package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule

import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class ScheduleViewState(
    val schedule: ScheduleModel = ScheduleModel(),
    val comment: String = "",
    val commentValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
