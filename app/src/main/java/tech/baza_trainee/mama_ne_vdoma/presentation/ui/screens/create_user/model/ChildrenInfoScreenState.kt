package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import tech.baza_trainee.mama_ne_vdoma.domain.model.Child

data class ChildrenInfoScreenState(
    val children: List<Child> = emptyList()
)
