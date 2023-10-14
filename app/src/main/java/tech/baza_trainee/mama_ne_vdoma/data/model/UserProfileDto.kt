package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("_id")
    val id: String = "",
    val email: String = "",
    val name: String? = null,
    val countryCode: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val location: LocationDto? = null,
    val week: Map<String, DayScheduleDto>? = null
)