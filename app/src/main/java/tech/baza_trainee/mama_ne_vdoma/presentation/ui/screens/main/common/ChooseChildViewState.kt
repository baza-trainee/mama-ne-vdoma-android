package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class ChooseChildViewState(
    val children: List<ChildEntity> = emptyList(),
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val isLoading: Boolean = false
)
