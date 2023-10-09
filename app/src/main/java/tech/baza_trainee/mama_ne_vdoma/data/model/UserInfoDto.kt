package tech.baza_trainee.mama_ne_vdoma.data.model

data class UserInfoDto(
    val name: String,
    val countryCode: String = "",
    val phone: String,
    val avatar: String?,
    val week: Map<String, DayScheduleDto>
)
