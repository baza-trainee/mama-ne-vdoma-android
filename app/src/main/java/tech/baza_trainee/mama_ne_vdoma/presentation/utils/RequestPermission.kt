package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onGranted: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Mama_ne_vdomaTheme {
        AlertDialog(
            onDismissRequest = onDismiss,
            buttons = {
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
                    Spacer( modifier = Modifier.width(4.dp))
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
            },
            title = {
                Text(text = "Увага! Потрібен дозвіл")
            },
            text = {
                Text(
                    text = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    )
                )
            },
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
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