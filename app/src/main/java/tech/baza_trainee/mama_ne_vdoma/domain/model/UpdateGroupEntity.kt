package tech.baza_trainee.mama_ne_vdoma.domain.model

data class UpdateGroupEntity(
    val name: String = "",
    val desc: String = "",
    val ages: String = "",
    val avatar: String = "",
    val schedule: ScheduleModel
)
