package tech.baza_trainee.mama_ne_vdoma.presentation.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class JoinRequestUiModel(
    val group: GroupUiModel = GroupUiModel(),
    val parentId: String = "",
    val parentEmail: String = "",
    val parentName: String = "",
    val parentPhone: String = "",
    val parentAddress: String = "",
    val parentAvatar: Uri = Uri.EMPTY,
    val child: ChildUiModel = ChildUiModel()
)
