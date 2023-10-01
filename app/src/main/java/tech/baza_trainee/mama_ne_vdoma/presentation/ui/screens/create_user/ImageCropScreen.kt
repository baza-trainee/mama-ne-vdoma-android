package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.OvalCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import org.koin.androidx.compose.getViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun ImageCropScreen(
    modifier: Modifier = Modifier,
    viewModel: UserSettingsViewModel,
    onImageCropped: () -> Unit = {}
) {
    SurfaceWithSystemBars(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
                text = "Обрізати фото",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = redHatDisplayFontFamily
            )

            var crop by remember { mutableStateOf(false) }
            var isCropping by remember { mutableStateOf(false) }

            val context = LocalContext.current
            val bitmapToCrop = viewModel.getBitmapForCrop(context.contentResolver)
            val croppedImage by remember { mutableStateOf(bitmapToCrop.asImageBitmap()) }

            val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }
            val cropProperties by remember {
                mutableStateOf(
                    CropDefaults.properties(
                        cropOutlineProperty = CropOutlineProperty(
                            OutlineType.Oval,
                            OvalCropShape(0, "Oval")
                        ),
                        handleSize = handleSize,
                        aspectRatio = AspectRatio(1f),
                        fixedAspectRatio = true
                    )
                )
            }
            val cropStyle by remember { mutableStateOf(CropDefaults.style()) }

            ImageCropper(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                backgroundColor = MaterialTheme.colorScheme.surface,
                imageBitmap = croppedImage,
                contentDescription = "Image Cropper",
                cropStyle = cropStyle,
                cropProperties = cropProperties,
                crop = crop,
                onCropStart = {
                    isCropping = true
                },
                onCropSuccess = {
                    isCropping = false
                    crop = false
                }
            )

            Spacer(modifier = modifier.height(32.dp))

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    viewModel.saveUserAvatar(croppedImage.asAndroidBitmap())
                    onImageCropped()
                }
            ) {
                Text(
                    text = "Зберегти",
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }
    }
}

@Composable
@Preview
fun ImageCropPreview() {
    ImageCropScreen(
        viewModel = getViewModel()
    )
}