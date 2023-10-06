package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class ImageCropViewModel(
    private val communicator: UserProfileCommunicator,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    fun getUserAvatarBitmap() = bitmapHelper.bitmapFromUri(communicator.uriForCrop).asImageBitmap()

    fun saveCroppedImage(image: Bitmap) {
        communicator.croppedImage = image
    }
}