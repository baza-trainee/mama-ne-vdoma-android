package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import tech.baza_trainee.mama_ne_vdoma.domain.model.Child

data class ChildrenInfoViewState(
    val children: List<Child> = emptyList()
)
