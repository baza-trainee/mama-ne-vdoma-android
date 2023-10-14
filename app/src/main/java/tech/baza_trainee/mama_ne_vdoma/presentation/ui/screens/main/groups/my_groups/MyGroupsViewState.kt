package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity

data class MyGroupsViewState(
    val groups: List<GroupEntity> = emptyList(),
    val isLoading: Boolean = false
)
