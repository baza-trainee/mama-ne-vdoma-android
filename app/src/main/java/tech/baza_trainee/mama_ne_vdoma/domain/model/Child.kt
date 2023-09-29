package tech.baza_trainee.mama_ne_vdoma.domain.model

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Gender

data class Child(
    val name: String = "",
    val age: Int = 0,
    val gender: Gender = Gender.NONE,
    val schedule: ScheduleModel = ScheduleModel()
)