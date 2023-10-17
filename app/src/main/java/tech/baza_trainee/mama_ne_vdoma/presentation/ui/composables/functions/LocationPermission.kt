package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.LocationPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings

@Composable
fun LocationPermission(
    onPermissionGranted: (Boolean) -> Unit = {}
) {
    val activity = LocalContext.current.findActivity()
    val permissionDialogQueue = remember { mutableStateListOf<String>() }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            if (perms.values.contains(false)) {
                val permission = perms.filter { !it.value }.keys.first()
                if (activity.shouldShowRequestPermissionRationale(permission))
                    permissionDialogQueue.add(permission)
            } else if (perms.isNotEmpty())
                onPermissionGranted(true)
        }
    )

    LaunchedEffect(key1 = true) {
        multiplePermissionResultLauncher.launch(permissions)
    }

    permissionDialogQueue.reversed().forEach {
        PermissionDialog(
            permissionTextProvider = LocationPermissionTextProvider(),
            isPermanentlyDeclined = !ActivityCompat
                .shouldShowRequestPermissionRationale(activity, it ),
            onDismiss = { permissionDialogQueue.remove(it) },
            onGranted = {
                permissionDialogQueue.remove(it)
                multiplePermissionResultLauncher.launch(permissions)
            },
            onGoToAppSettingsClick = { activity.openAppSettings() })
    }
}