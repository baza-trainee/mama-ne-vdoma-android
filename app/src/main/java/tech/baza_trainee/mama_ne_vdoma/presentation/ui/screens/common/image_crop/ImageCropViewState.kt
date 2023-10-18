package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class ImageCropViewState(
    val image: ImageBitmap = BitmapHelper.DEFAULT_BITMAP.asImageBitmap()
)
