package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.image_crop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.model.aspectRatios
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewState

@Composable
fun GroupImageCropScreen(
    screenState: ImageCropViewState,
    handleEvent: (ImageCropEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Створення нової групи",
                avatar = screenState.avatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(ImageCropEvent.OnAvatarClicked) },
                onBack = { handleEvent(ImageCropEvent.OnBack) }
            )
        }
    ) { paddingValues ->

        BackHandler { handleEvent(ImageCropEvent.OnBack) }

        val handleSize = LocalDensity.current.run { 20.dp.toPx() }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(4.dp))

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
    }
}

@Composable
@Preview
fun GroupImageCropPreview() {
    GroupImageCropScreen(
        screenState = ImageCropViewState(),
        handleEvent = {}
    )
}