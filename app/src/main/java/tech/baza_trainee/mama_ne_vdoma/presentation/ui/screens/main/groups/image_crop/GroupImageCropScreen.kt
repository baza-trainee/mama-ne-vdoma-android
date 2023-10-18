package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.model.aspectRatios
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewState

@Composable
fun GroupImageCropScreen(
    modifier: Modifier = Modifier,
    screenState: State<ImageCropViewState> = mutableStateOf(ImageCropViewState()),
    handleEvent: (Bitmap) -> Unit = {}
) {
    val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }

    ImageCropScreen(
        imageForCrop = screenState.value.image,
        cropProperties = CropDefaults.properties(
            cropOutlineProperty = CropOutlineProperty(
                OutlineType.Rect,
                RectCropShape(0, "Rect")
            ),
            handleSize = handleSize,
            aspectRatio = aspectRatios[4].aspectRatio,
            fixedAspectRatio = true
        ),
        onImageCrop = { handleEvent(it) }
    )
}

@Composable
@Preview
fun GroupImageCropPreview() {
    GroupImageCropScreen()
}