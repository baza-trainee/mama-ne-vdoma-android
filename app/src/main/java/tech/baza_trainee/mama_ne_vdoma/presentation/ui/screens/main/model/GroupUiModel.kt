package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri

data class GroupUiModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val ages: String = "",
    val avatar: Uri = Uri.EMPTY,
    val location: String = "",
    val members: List<MemberUiModel> = mutableListOf(),
    val isChecked: Boolean = false
)

data class MemberUiModel(
    val id: String = "",
    val name: String = "",
    val avatar: Uri = Uri.EMPTY
)
