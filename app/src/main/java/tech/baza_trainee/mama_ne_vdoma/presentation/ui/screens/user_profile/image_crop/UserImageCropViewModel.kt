package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class UserImageCropViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    private val _viewState = MutableStateFlow(ImageCropViewState())
    val viewState: StateFlow<ImageCropViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    image = bitmapHelper.bitmapFromUri(communicator.uriForCrop).asImageBitmap()
                )
            }
        }
    }

    fun saveCroppedImage(image: Bitmap) {
        communicator.croppedImage = image
        navigator.navigate(UserProfileRoutes.UserInfo)
    }
}