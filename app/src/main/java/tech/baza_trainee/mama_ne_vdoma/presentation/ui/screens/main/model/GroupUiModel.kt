package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import androidx.compose.runtime.Stable

data class GroupUiModel(
    val id: String = "",
    val adminId: String = "",
    val name: String = "",
    val description: String = "",
    val ages: String = "",
    @Stable val avatar: Uri = Uri.EMPTY,
    val location: String = "",
    @Stable val members: List<MemberUiModel> = emptyList(),
    val isChecked: Boolean = false
)

data class MemberUiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: Uri = Uri.EMPTY,
    @Stable val children: List<String> = emptyList()
)
