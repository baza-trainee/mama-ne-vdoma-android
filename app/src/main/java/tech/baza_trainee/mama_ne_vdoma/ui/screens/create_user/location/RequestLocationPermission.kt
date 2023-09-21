package tech.baza_trainee.mama_ne_vdoma.ui.screens.create_user.location

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    onPermissionGranted: () -> Unit,
    onRefuse: () -> Unit
) {
    val permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val permissionState = rememberMultiplePermissionsState(permissions)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = "Якщо Ви не надасте доступ до визначення місцезнаходження пристрою," +
                        " подальше повноцінне користування додатком буде не можливе",
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchMultiplePermissionRequest() },
                onRefuse = onRefuse
            )
        },
        onPermissionGranted = onPermissionGranted
    )
}
@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    onPermissionGranted: () -> Unit
) {
    val deniedList = permissionState.permissions.map { it.status }.filterIsInstance<PermissionStatus.Denied>()
    if (deniedList.isEmpty())
        onPermissionGranted()
    else
        deniedContent(deniedList.first().shouldShowRationale)
}

@Composable
fun Content(
    showButton: Boolean = true,
    onClick: () -> Unit
) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomLocationDialog(
                title = "Дозвольте доступ до визначення місцезнаходження пристрою",
                desc = "Дозвольте доступ до визначення місцезнаходження пристрою для подальшого використання додатку." +
                        " В разі, якщо Ви не зробите це зараз, цг можна зробити в будь-який час в Налаштуваннях пристрою",
                enableLocation,
                onClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onRefuse: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = onRefuse,
            title = {
                Text(
                    text = "Дозвольте доступ до визначення місцезнаходження пристрою"
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Дати доступ")
                }
            }
        )

    }
    else {
        Content(onClick = onRequestPermission)
    }
}