package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class UserProfileEntity(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val note: String = "",
    val countryCode: String = "",
    val phone: String = "",
    val sendingEmails: Boolean = true,
    val avatar: String = "",
    val location: LocationEntity = LocationEntity(),
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val groupJoinRequests: List<JoinRequestEntity> = emptyList(),
    val notifications: List<NotificationEntity> = emptyList(),
    val rating: Float = 0f
)

data class JoinRequestEntity(
    val groupId: String = "",
    val childId: String = ""
)

data class NotificationEntity(
    val groupId: String = "",
    val type: String = ""
)