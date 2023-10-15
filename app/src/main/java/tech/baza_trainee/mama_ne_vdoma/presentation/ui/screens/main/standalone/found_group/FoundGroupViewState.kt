package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class FoundGroupViewState(
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val groups: List<GroupUiModel> = emptyList(),
    val isLoading: Boolean = false
)
