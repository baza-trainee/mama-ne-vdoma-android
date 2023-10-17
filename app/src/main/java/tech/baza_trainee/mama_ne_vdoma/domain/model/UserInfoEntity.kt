package tech.baza_trainee.mama_ne_vdoma.domain.model

data class UserInfoEntity(
    val name: String = "",
    val countryCode: String = "",
    val phone: String = "",
    val sendingEmails: Boolean = true,
    val avatar: String? = null,
    val schedule: ScheduleModel = ScheduleModel()
)