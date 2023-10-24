package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel

data class MyGroupsViewState(
    val userId: String = "",
    val groups: List<GroupUiModel> = emptyList(),
    val isLoading: Boolean = false
)
