package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("_id")
    val id: String = "",
    val email: String = "",
    val name: String? = null,
    val countryCode: String? = null,
    val phone: String? = null,
    val note: String? = null,
    val sendingEmails: Boolean = true,
    val avatar: String? = null,
    val location: LocationDto? = null,
    val week: Map<String, DayScheduleDto>? = null,
    val groupJoinRequests: List<JoinRequestDto> = emptyList(),
    val notifications: List<NotificationDto> = emptyList(),
    val karma: Float? = null
)

data class UserProfileResponse(
    val user: UserProfileDto
)

data class JoinRequestDto(
    val groupId: String = "",
    val childId: String = ""
)

data class NotificationDto(
    val groupId: String = "",
    val notificationType: String = "",
    val creationTime: Long = 0L
)