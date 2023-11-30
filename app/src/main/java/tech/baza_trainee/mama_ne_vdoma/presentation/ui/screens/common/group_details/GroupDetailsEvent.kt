package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import java.time.DayOfWeek

sealed interface GroupDetailsEvent {
    data object GetLocationFromAddress : GroupDetailsEvent
    data class UpdateGroupAddress(val address: String) : GroupDetailsEvent
    data class UpdateGroupSchedule(val day: DayOfWeek, val period: Period) : GroupDetailsEvent
    data class UpdateName(val value: String) : GroupDetailsEvent
    data class UpdateDescription(val value: String) : GroupDetailsEvent
    data class UpdateMinAge(val value: String) : GroupDetailsEvent
    data class UpdateMaxAge(val value: String) : GroupDetailsEvent
    data class SetImageToCrop(val uri: Uri) : GroupDetailsEvent
    data object OnBack: GroupDetailsEvent
    data object OnSave: GroupDetailsEvent
    data object ResetUiState : GroupDetailsEvent
    data object OnEditPhoto: GroupDetailsEvent
    data object OnDeletePhoto: GroupDetailsEvent
    data object GoToMain: GroupDetailsEvent
    data object OnAvatarClicked : GroupDetailsEvent
    data object GoToNotifications : GroupDetailsEvent
    data class OnKick(val children: List<String>): GroupDetailsEvent
}