package tech.baza_trainee.mama_ne_vdoma.data.model

data class UserProfileDto(
    val email: String = "",
    val name: String = "",
    val countryCode: String = "",
    val phone: String = "",
    val avatar: String = "",
    val location: LocationDto = LocationDto(),
    val week: Map<String, DayScheduleDto>?
)

data class LocationDto(
    val type: String = "",
    val coordinates: List<Double> = emptyList()
)