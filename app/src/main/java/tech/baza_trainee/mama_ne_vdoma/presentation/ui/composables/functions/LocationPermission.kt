package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    val permission = Manifest.permission.ACCESS_COARSE_LOCATION
    var showRationale by rememberSaveable { mutableStateOf(false) }

    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                if (activity.shouldShowRequestPermissionRationale(permission))
                    showRationale = true
            } else onPermissionGranted(true)
        }
    )

    LaunchedEffect(key1 = true) {
        locationPermissionResultLauncher.launch(permission)
    }

    if (showRationale) {
        PermissionDialog(
            permissionTextProvider = LocationPermissionTextProvider(),
            isPermanentlyDeclined = !ActivityCompat
                .shouldShowRequestPermissionRationale(activity, permission),
            onDismiss = { showRationale = false },
            onGranted = { showRationale = false },
            onGoToAppSettingsClick = { activity.openAppSettings() })
    }
}