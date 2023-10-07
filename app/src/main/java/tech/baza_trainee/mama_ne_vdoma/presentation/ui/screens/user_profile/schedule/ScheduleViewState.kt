package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule

import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel

data class ScheduleViewState(
    val schedule: ScheduleModel = ScheduleModel(),
    val isLoading: Boolean = false
)
