package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class GroupImageCropViewModel(
    private val communicator: GroupImageCommunicator,
    private val navigator: ScreenNavigator,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    fun getUserAvatarBitmap() = bitmapHelper.bitmapFromUri(communicator.uriForCrop).asImageBitmap()

    fun saveCroppedImage(image: Bitmap) {
        communicator.croppedImage = image
        navigator.navigate(UserProfileRoutes.UserInfo)
    }
}