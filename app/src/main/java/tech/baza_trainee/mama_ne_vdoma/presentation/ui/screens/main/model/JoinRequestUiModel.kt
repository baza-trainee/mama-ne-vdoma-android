package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class JoinRequestUiModel(
    val group: GroupUiModel = GroupUiModel(),
    val parentId: String = "",
    val parentEmail: String = "",
    val parentName: String = "",
    val parentPhone: String = "",
    val parentAddress: String = "",
    val parentAvatar: Uri = Uri.EMPTY,
    val child: ChildEntity = ChildEntity()
)
