package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import android.Manifest
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.CameraPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.ImageSourceDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun GroupAvatarWithCameraAndGallery(
    modifier: Modifier = Modifier,
    canDelete: Boolean = false,
    setUriForCrop: (Uri) -> Unit = {},
    onEditPhoto: () -> Unit = {},
    onDeletePhoto: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val permission = Manifest.permission.CAMERA
    var photoUri by remember { mutableStateOf(Uri.EMPTY) }
    var showRationale by rememberSaveable { mutableStateOf(false) }
    var showPickerDialog by rememberSaveable { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            setUriForCrop(photoUri)
            onEditPhoto()
        }
    }

    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isCameraPermissionGranted) {
        if (isCameraPermissionGranted) {
            withContext(Dispatchers.IO) {
                val photoFile = File.createTempFile(
                    "IMG_",
                    ".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                photoPickerLauncher.launch(photoUri)
            }
        }
    }

    if (showRationale) {
        PermissionDialog(
            permissionTextProvider = CameraPermissionTextProvider(LocalContext.current),
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(activity, permission),
            onDismiss = { showRationale = false },
            onGranted = {
                showRationale = false
                isCameraPermissionGranted = true
            },
            onGoToAppSettingsClick = { activity.openAppSettings() }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showPickerDialog = true
            }
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.no_photo),
            contentDescription = "avatar",
            contentScale = ContentScale.Fit
        )

        if (canDelete) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDeletePhoto() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                if (shouldShowRequestPermissionRationale(activity, permission))
                    showRationale = true
            } else isCameraPermissionGranted = true
        }
    )

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                setUriForCrop(it)
                onEditPhoto()
            }
        }

    if (showPickerDialog) {
        ImageSourceDialog(
            onPickFromCamera = {
                cameraPermissionResultLauncher.launch(permission)
                showPickerDialog = false
            },
            onPickFromGallery = {
                imagePickerLauncher.launch(arrayOf("image/*"))
                showPickerDialog = false
            },
            onHideDialog = { showPickerDialog = false }
        )
    }
}