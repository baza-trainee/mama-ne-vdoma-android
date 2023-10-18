package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider = LocationPermissionTextProvider(),
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
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = "Увага! Потрібен дозвіл",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                ),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Відхилити",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            onDismiss()
                        }
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (isPermanentlyDeclined) {
                        "Дозволити в налаштуваннях"
                    } else {
                        "Дозволити"
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
                        .padding(16.dp)
                )
            }
        }
    }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "Схоже, ви назавжди відхилили дозвіл на місцезнаходження. " +
                    "Ви можете перейти в налаштування додатку, щоб надати його"
        } else {
            "Цій програмі потрібен доступ до вашого місцезнаходження, щоб " +
                    "працювати повноцінно"
        }
    }
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Схоже, ви назавжди відхилили дозвіл на використання камери. " +
                    "Ви можете перейти в налаштування додатку, щоб надати його"
        } else {
            "Цій програмі потрібен доступ на використання камери, щоб " +
                    "працювати повноцінно"
        }
    }
}