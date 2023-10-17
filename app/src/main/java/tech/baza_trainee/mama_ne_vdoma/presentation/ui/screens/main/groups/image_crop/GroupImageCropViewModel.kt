package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class GroupImageCropViewModel(
    private val communicator: GroupImageCommunicator,
    private val navigator: PageNavigator,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    fun getUserAvatarBitmap() = bitmapHelper.bitmapFromUri(communicator.uriForCrop).asImageBitmap()

    fun saveCroppedImage(image: Bitmap) {
        communicator.setCroppedImage(image)
        navigator.goBack()
    }
}