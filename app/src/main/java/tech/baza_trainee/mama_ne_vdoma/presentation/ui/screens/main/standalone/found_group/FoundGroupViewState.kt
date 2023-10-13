package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity

data class FoundGroupViewState(
    val groups: List<GroupEntity> = emptyList(),
    val isLoading: Boolean = false
)
