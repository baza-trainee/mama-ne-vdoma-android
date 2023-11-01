package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop

import android.graphics.Bitmap

sealed interface ImageCropEvent {
    data object OnBack: ImageCropEvent
    data class OnImageCropped(val image: Bitmap): ImageCropEvent
    data object OnAvatarClicked : ImageCropEvent
    data object RotateLeft : ImageCropEvent
    data object RotateRight : ImageCropEvent
}