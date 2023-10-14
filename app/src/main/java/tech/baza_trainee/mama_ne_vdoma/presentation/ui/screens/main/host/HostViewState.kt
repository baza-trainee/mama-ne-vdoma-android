package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class HostViewState(
    val currentPage: Int = 0,
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val isLoading: Boolean = false
)
