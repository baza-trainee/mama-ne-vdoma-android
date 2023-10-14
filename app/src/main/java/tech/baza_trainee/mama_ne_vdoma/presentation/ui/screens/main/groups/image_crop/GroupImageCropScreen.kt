package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.model.aspectRatios
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun GroupImageCropScreen(
    modifier: Modifier = Modifier,
    imageForCrop: ImageBitmap = ImageBitmap(512, 512),
    handleEvent: (Bitmap) -> Unit = {}
) {
    var isCropping by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
            text = "Обрізати фото",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = redHatDisplayFontFamily
        )

        Spacer(modifier = Modifier.height(8.dp))

        val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }
        var crop by remember { mutableStateOf(false) }

        ImageCropper(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            imageBitmap = imageForCrop,
            contentDescription = "Image Cropper",
            cropStyle = CropDefaults.style(),
            cropProperties = CropDefaults.properties(
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Rect,
                    RectCropShape(0, "Rect")
                ),
                handleSize = handleSize,
                aspectRatio = aspectRatios[4].aspectRatio,
                fixedAspectRatio = true
            ),
            crop = crop,
            onCropStart = {
                isCropping = true
            },
            onCropSuccess = {
                handleEvent(it.asAndroidBitmap())
                crop = false
                isCropping = false
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                crop = true
            }
        ) {
            ButtonText(
                text = "Зберегти"
            )
        }
        if (isCropping) LoadingIndicator()
    }
}

@Composable
@Preview
fun GroupImageCropPreview() {
    GroupImageCropScreen()
}