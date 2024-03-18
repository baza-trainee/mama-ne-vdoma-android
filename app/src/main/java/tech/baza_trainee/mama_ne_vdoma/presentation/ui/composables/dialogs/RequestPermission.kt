package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider =
        LocationPermissionTextProvider(LocalContext.current),
    isPermanentlyDeclined: Boolean = false,
    onDismiss: () -> Unit = {},
    onGranted: () -> Unit = {},
    onGoToAppSettingsClick: () -> Unit = {}
) {
    GenericAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(id = R.string.attention_need_permission),
        text = permissionTextProvider.getDescription(
            isPermanentlyDeclined = isPermanentlyDeclined
        ),
        dismissButtonText = stringResource(id = R.string.action_refuse),
        dismissButtonAction = onDismiss,
        confirmButtonText = if (isPermanentlyDeclined) {
            stringResource(id = R.string.action_allow_in_settings)
        } else {
            stringResource(id = R.string.action_allow)
        },
        confirmButtonAction = {
            if (isPermanentlyDeclined) {
                onGoToAppSettingsClick()
            } else {
                onGranted()
            }
        }
    )
}

interface PermissionTextProvider {

    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider(private val context: Context): PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.permission_location_declined)
        } else {
            context.getString(R.string.permission_location_info)
        }
    }
}

class CameraPermissionTextProvider(private val context: Context): PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            context.getString(R.string.permission_camera_declined)
        } else {
            context.getString(R.string.permission_camera_info)
        }
    }
}

class NotificationsPermissionTextProvider(private val context: Context): PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            context.getString(R.string.permission_notification_declined)
        } else {
            context.getString(R.string.permission_notification_info)
        }
    }
}