package tech.baza_trainee.mama_ne_vdoma.domain.model

data class Child(
    val id: String = "",
    val name: String = "",
    val age: String = "",
    val gender: Gender = Gender.NONE,
    val schedule: ScheduleModel = ScheduleModel(),
    val comment: String = ""
)