package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class GroupImageCommunicator {
    var uriForCrop: Uri = Uri.EMPTY

    private val _croppedImageFlow = MutableStateFlow(BitmapHelper.DEFAULT_BITMAP)
    val croppedImageFlow: StateFlow<Bitmap> = _croppedImageFlow.asStateFlow()

    fun setCroppedImage(image: Bitmap?) {
        _croppedImageFlow.update {
            image ?: BitmapHelper.DEFAULT_BITMAP
        }
    }
}