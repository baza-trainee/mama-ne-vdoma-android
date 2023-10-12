package tech.baza_trainee.mama_ne_vdoma.domain.model

data class GroupEntity(
    val id: String,
    val name: String,
    val description: String,
    val ages: String,
    val schedule: ScheduleModel
)
