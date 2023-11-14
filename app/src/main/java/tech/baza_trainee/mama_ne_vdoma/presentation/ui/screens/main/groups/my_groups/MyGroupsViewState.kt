package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import androidx.compose.runtime.Stable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel

data class MyGroupsViewState(
    val userId: String = "",
    @Stable val groups: List<GroupUiModel> = emptyList(),
    val isLoading: Boolean = false
)
