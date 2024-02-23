package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.model.aspectRatios
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_20_dp

@Composable
fun UpdateGroupAvatarScreen(
    screenState: ImageCropViewState,
    handleEvent: (ImageCropEvent) -> Unit
) {
    BackHandler { handleEvent(ImageCropEvent.OnBack) }

    val handleSize = LocalDensity.current.run { size_20_dp.toPx() }

    ImageCropScreen(
        imageForCrop = screenState.image,
        cropProperties = CropDefaults.properties(
            cropOutlineProperty = CropOutlineProperty(
                OutlineType.Rect,
                RectCropShape(0, "Rect")
            ),
            handleSize = handleSize,
            aspectRatio = aspectRatios[4].aspectRatio,
            fixedAspectRatio = true
        ),
        handleEvent = { handleEvent(it) }
    )
}