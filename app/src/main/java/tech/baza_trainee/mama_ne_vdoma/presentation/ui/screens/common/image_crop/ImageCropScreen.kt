package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropProperties
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
fun ImageCropScreen(
    modifier: Modifier = Modifier,
    imageForCrop: ImageBitmap,
    cropProperties: CropProperties,
    handleEvent: (ImageCropEvent) -> Unit
) {
    var isCropping by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = size_16_dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                text = stringResource(id = R.string.crop_photo),
                fontSize = font_size_20_sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = redHatDisplayFontFamily
            )

            var crop by remember { mutableStateOf(false) }

            ImageCropper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_8_dp)
                    .weight(1f),
                imageBitmap = imageForCrop,
                contentDescription = "Image Cropper",
                cropStyle = CropDefaults.style(),
                cropProperties = cropProperties,
                crop = crop,
                onCropStart = {
                    isCropping = true
                },
                onCropSuccess = {
                    handleEvent(ImageCropEvent.OnImageCropped(it.asAndroidBitmap()))
                    crop = false
                    isCropping = false
                }
            )

            Row(
                modifier = Modifier
                    .padding(bottom = size_16_dp, top = size_32_dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { handleEvent(ImageCropEvent.RotateRight) }
                ) {
                    Icon(
                        modifier = Modifier
                            .height(size_32_dp)
                            .width(size_32_dp),
                        painter = painterResource(id = R.drawable.ic_rotate_right),
                        contentDescription = stringResource(id = R.string.rotate_photo_right)
                    )
                }

                Text(
                    modifier = Modifier.padding(horizontal = size_16_dp),
                    text = stringResource(id = R.string.rotate_photo),
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                IconButton(
                    onClick = { handleEvent(ImageCropEvent.RotateLeft) }
                ) {
                    Icon(
                        modifier = Modifier
                            .height(size_32_dp)
                            .width(size_32_dp),
                        painter = painterResource(id = R.drawable.ic_rotate_left),
                        contentDescription = stringResource(id = R.string.rotate_photo_left)
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(bottom = size_16_dp, top = size_48_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { crop = true }
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_save)
                )
            }
        }
    }

    if (isCropping) LoadingIndicator()
}