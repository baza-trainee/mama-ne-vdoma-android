package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import java.time.DayOfWeek

sealed interface CreateGroupEvent {
    data object GetLocationFromAddress : CreateGroupEvent
    data class UpdateGroupAddress(val address: String) : CreateGroupEvent
    data class UpdateGroupSchedule(val day: DayOfWeek, val period: Period) : CreateGroupEvent
    data class UpdateName(val value: String) : CreateGroupEvent
    data class UpdateDescription(val value: String) : CreateGroupEvent
    data class UpdateMinAge(val value: String) : CreateGroupEvent
    data class UpdateMaxAge(val value: String) : CreateGroupEvent
    data class SetImageToCrop(val uri: Uri) : CreateGroupEvent
    data object OnBack: CreateGroupEvent
    data object OnCreate: CreateGroupEvent
    data object ResetUiState : CreateGroupEvent
    data object OnEditPhoto: CreateGroupEvent
    data object OnDeletePhoto: CreateGroupEvent
    data object GoToMain: CreateGroupEvent
}