package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class ImageCropViewModel(
    private val navigator: ScreenNavigator,
    private val communicator: CropImageCommunicator,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    fun getUserAvatarBitmap() = bitmapHelper.bitmapFromUri(communicator.uriForCrop).asImageBitmap()

    fun saveCroppedImage(image: Bitmap) {
        communicator.setCroppedImage(image)
        navigator.goBack()
    }
}