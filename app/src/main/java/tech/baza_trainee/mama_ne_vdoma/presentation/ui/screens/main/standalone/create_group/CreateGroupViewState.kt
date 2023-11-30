package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsViewState

data class CreateGroupViewState(
    val groupDetails: GroupDetailsViewState = GroupDetailsViewState(),
    val notifications: Int = 0,
    val isLoading: Boolean = false
)
