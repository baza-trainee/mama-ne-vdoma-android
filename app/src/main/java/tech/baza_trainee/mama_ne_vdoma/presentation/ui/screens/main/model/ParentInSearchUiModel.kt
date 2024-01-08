package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class ParentInSearchUiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: Uri = Uri.EMPTY,
    val groups: List<GroupsInSearchUiModel> = emptyList()
)

data class GroupsInSearchUiModel(
    val name: String = "",
    val id: String = ""
)
