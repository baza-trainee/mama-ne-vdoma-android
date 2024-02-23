package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.image_crop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.OvalCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_20_dp

@Composable
fun ProfileImageCropScreen(
    screenState: ImageCropViewState = ImageCropViewState(),
    handleEvent: (ImageCropEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val handleSize: Float = LocalDensity.current.run { size_20_dp.toPx() }

        ImageCropScreen(
            imageForCrop = screenState.image,
            cropProperties = CropDefaults.properties(
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Oval,
                    OvalCropShape(0, "Oval")
                ),
                handleSize = handleSize,
                aspectRatio = AspectRatio(1f),
                fixedAspectRatio = true
            ),
            handleEvent = { handleEvent(it) }
        )
    }
}

@Composable
@Preview
fun ProfileImageCropPreview() {
    ProfileImageCropScreen(
        screenState = ImageCropViewState(),
        handleEvent = {}
    )
}