package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel

data class ScheduleViewState(
    val schedule: ScheduleModel = ScheduleModel(),
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)
