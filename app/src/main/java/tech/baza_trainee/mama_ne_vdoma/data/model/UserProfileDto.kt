package tech.baza_trainee.mama_ne_vdoma.data.model

data class UserProfileDto(
    val email: String = "",
    val name: String? = null,
    val countryCode: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val location: LocationDto? = null,
    val week: Map<String, DayScheduleDto>? = null
)

data class LocationDto(
    val type: String = "",
    val coordinates: List<Double> = emptyList()
)