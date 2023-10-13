package tech.baza_trainee.mama_ne_vdoma.data.model

data class UpdateGroupDto(
    val name: String = "",
    val desc: String = "",
    val ages: String = "",
    val week: Map<String, DayScheduleDto>
)
