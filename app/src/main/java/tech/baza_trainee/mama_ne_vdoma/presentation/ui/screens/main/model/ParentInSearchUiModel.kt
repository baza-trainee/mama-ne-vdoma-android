package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class ParentInSearchUiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val children: List<ChildInSearchUiModel> = emptyList()
)

data class ChildInSearchUiModel(
    val name: String = "",
    val age: String = ""
)
