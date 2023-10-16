package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri

data class ParentInSearchUiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: Uri = Uri.EMPTY,
    val children: List<ChildInSearchUiModel> = emptyList()
)

data class ChildInSearchUiModel(
    val name: String = "",
    val age: String = ""
)
