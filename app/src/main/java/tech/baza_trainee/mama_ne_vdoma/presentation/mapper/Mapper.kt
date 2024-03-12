package tech.baza_trainee.mama_ne_vdoma.presentation.mapper

import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.model.ChildUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.toStateMap

fun ChildEntity.toUiModel() = ChildUiModel(
    name = name,
    age = age,
    gender = gender,
    note = note,
    parentId = parentId,
    childId = childId,
    schedule = schedule.toStateMap()
)