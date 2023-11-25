package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsViewState

data class UpdateGroupViewState(
    val groupDetails: GroupDetailsViewState = GroupDetailsViewState(),
    val isLoading: Boolean = false
)
