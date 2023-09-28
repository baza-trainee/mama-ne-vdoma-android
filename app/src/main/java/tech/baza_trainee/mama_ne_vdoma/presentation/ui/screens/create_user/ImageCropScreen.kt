package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.OvalCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel

@Composable
fun ImageCropFunc(
    viewModel: UserSettingsViewModel,
    onImageCropped: () -> Unit
) {
    val context = LocalContext.current
    ImageCrop(
        bitmapToCrop = viewModel.getBitmapForCrop(context.contentResolver),
        saveAvatar = { viewModel.saveUserAvatar(it) },
        onImageCropped = onImageCropped
    )
}

@Composable
fun ImageCrop(
    modifier: Modifier = Modifier,
    bitmapToCrop: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
    saveAvatar: (Bitmap) -> Unit = {},
    onImageCropped: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var crop by remember { mutableStateOf(false) }
            var isCropping by remember { mutableStateOf(false) }
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
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = {
                    saveAvatar(croppedImage.asAndroidBitmap())
                    onImageCropped()
                }
            ) {
                Text(text = "Далі")
            }
        }
    }
}

@Composable
@Preview
fun ImageCropPreview() {
    ImageCrop()
}