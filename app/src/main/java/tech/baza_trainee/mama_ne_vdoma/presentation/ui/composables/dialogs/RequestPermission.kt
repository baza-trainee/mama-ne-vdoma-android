package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

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
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(size_8_dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(size_16_dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = size_8_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.attention_need_permission),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth(),
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                ),
                fontSize = font_size_14_sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(size_16_dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(id = R.string.action_refuse),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            onDismiss()
                        }
                        .padding(size_16_dp)
                )

                Spacer(modifier = Modifier.width(size_4_dp))

                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(id = R.string.action_allow_in_settings)
                    } else {
                        stringResource(id = R.string.action_allow)
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onGranted()
                            }
                        }
                        .padding(size_16_dp)
                )
            }
        }
    }
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