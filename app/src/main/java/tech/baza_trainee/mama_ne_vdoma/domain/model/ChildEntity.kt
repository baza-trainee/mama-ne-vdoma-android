package tech.baza_trainee.mama_ne_vdoma.domain.model

data class ChildEntity(
    val name: String = "",
    val age: String = "",
    val gender: Gender = Gender.NONE,
    val note: String = "",
    val parentId: String = "",
    val childId: String = "",
    val schedule: ScheduleModel = ScheduleModel()
)
