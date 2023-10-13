package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child

import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class ChooseChildViewState(
    val children: List<ChildEntity> = emptyList(),
    val isLoading: Boolean = false
)
