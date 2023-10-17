package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.OvalCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars

@Composable
fun UserImageCropScreen(
    modifier: Modifier = Modifier,
    imageForCrop: ImageBitmap = ImageBitmap(512, 512),
    handleEvent: (Bitmap) -> Unit = {}
) {
    SurfaceWithSystemBars {
        val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }

        ImageCropScreen(
            imageForCrop = imageForCrop,
            cropProperties = CropDefaults.properties(
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Oval,
                    OvalCropShape(0, "Oval")
                ),
                handleSize = handleSize,
                aspectRatio = AspectRatio(1f),
                fixedAspectRatio = true
            ),
            onImageCrop = { handleEvent(it) }
        )
    }
}

@Composable
@Preview
fun UserImageCropPreview() {
    UserImageCropScreen()
}