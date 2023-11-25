package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE

class ImageCropViewModel(
    private val navigator: ScreenNavigator,
    private val communicator: CropImageCommunicator,
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

    fun handleEvent(event: ImageCropEvent) {
        when(event) {
            ImageCropEvent.OnBack -> navigator.goBack()
            is ImageCropEvent.OnImageCropped -> saveCroppedImage(event.image)
            ImageCropEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))

            ImageCropEvent.RotateLeft -> rotateBitmap(-90)
            ImageCropEvent.RotateRight -> rotateBitmap(90)
        }
    }

    private fun rotateBitmap(degrees: Int) {
        val rotated = bitmapHelper.rotateImage(_viewState.value.image.asAndroidBitmap(), degrees)
        _viewState.update {
            it.copy(
                image = rotated.asImageBitmap()
            )
        }
    }

    private fun saveCroppedImage(image: Bitmap) {
        communicator.apply {
            justCropped = true
            setCroppedImage(image)
        }
        navigator.goBack()
    }
}