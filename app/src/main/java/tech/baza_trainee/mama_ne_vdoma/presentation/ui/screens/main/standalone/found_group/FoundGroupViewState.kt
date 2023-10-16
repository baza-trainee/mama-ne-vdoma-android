package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel

data class FoundGroupViewState(
    val avatar: Uri = Uri.EMPTY,
    val groups: List<GroupUiModel> = emptyList(),
    val isLoading: Boolean = false
)
