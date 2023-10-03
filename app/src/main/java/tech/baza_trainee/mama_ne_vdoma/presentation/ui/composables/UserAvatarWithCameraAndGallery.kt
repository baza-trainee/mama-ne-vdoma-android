package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAvatarWithCameraAndGallery(
    modifier: Modifier = Modifier,
    avatar: Bitmap?,
    setUriForCrop: (Uri) -> Unit,
    onEditPhoto: () -> Unit
) {
    var showPickerDialog by rememberSaveable { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf(Uri.EMPTY) }
    val context = LocalContext.current
    val activity = context.findActivity()
    val permission = Manifest.permission.CAMERA
    var showRationale by rememberSaveable { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            setUriForCrop(photoUri)
            onEditPhoto()
        }
    }

    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                if (activity.shouldShowRequestPermissionRationale(permission))
                    showRationale = true
            } else isCameraPermissionGranted = true
        }
    )

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
            permissionTextProvider = CameraPermissionTextProvider(),
            isPermanentlyDeclined = !ActivityCompat
                .shouldShowRequestPermissionRationale(activity, permission),
            onDismiss = { showRationale = false },
            onGranted = { showRationale = false },
            onGoToAppSettingsClick = { activity.openAppSettings() })
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                setUriForCrop(it)
                onEditPhoto()
            }
        }

    if (avatar != null)
        Image(
            bitmap = avatar.asImageBitmap(),
            contentDescription = "avatar",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .width(172.dp)
                .height(172.dp)
                .clip(CircleShape)
                .clickable {
                    showPickerDialog = true
                }
        )
    else
        Image(
            painter = painterResource(id = R.drawable.no_photo),
            contentDescription = "avatar",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .width(172.dp)
                .height(172.dp)
                .clip(CircleShape)
                .clickable {
                    showPickerDialog = true
                }
        )

    if (showPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPickerDialog = false }
        ) {
            val dialogModifier = Modifier
            Column(
                modifier = dialogModifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = dialogModifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = "Обрати фото",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = redHatDisplayFontFamily
                )
                Text(
                    modifier = dialogModifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = "Оберіть спосіб завантаження файлу",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = redHatDisplayFontFamily
                )
                Row(
                    modifier = dialogModifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            cameraPermissionResultLauncher.launch(permission)
                            showPickerDialog = false
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null
                    )
                    Text(
                        modifier = dialogModifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .fillMaxWidth(),
                        text = "Камера",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
                HorizontalDivider(
                    modifier = dialogModifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = SlateGray
                )
                Row(
                    modifier = dialogModifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                            showPickerDialog = false
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_gallery),
                        contentDescription = null
                    )
                    Text(
                        modifier = dialogModifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .fillMaxWidth(),
                        text = "Галерея",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
            }
        }
    }
}