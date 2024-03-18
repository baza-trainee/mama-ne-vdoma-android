package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
fun GenericAlertDialog(
    icon: ImageVector? = null,
    title: String? = null,
    text: String,
    confirmButtonText: String,
    confirmButtonAction: () -> Unit,
    dismissButtonText: String? = null,
    dismissButtonAction: (() -> Unit) ? = null,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(size_4_dp),
        icon = {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = "alert",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
                title?.let {
                    Text(
                        modifier = Modifier
                            .padding(top = size_8_dp)
                            .padding(horizontal = size_16_dp)
                            .fillMaxWidth(),
                        text = title,
                        fontSize = font_size_20_sp,
                        textAlign = TextAlign.Center,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
        },
        text = {
            Text(
                text = text,
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            Text(
                modifier = Modifier
                    .padding(start = size_8_dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { confirmButtonAction() },
                text = confirmButtonText,
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        dismissButton = {
            dismissButtonText?.let {
                Text(
                    modifier = Modifier
                        .padding(end = size_8_dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            dismissButtonAction?.let {
                                dismissButtonAction()
                            } ?: onDismissRequest()
                        },
                    text = dismissButtonText,
                    fontSize = font_size_16_sp,
                    fontFamily = redHatDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }
    )
}

@Composable
fun GenericAlertDialog(
    icon: Painter,
    text: String,
    confirmButtonText: String,
    confirmButtonAction: () -> Unit,
    dismissButtonText: String? = null,
    dismissButtonAction: (() -> Unit) ? = null,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(size_4_dp),
        icon = {
            Icon(
                painter = icon,
                contentDescription = "alert",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(
                text = text,
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            Text(
                modifier = Modifier
                    .padding(start = size_8_dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { confirmButtonAction() },
                text = confirmButtonText,
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        dismissButton = {
            dismissButtonText?.let {
                Text(
                    modifier = Modifier
                        .padding(end = size_8_dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            dismissButtonAction?.let {
                                dismissButtonAction()
                            } ?: onDismissRequest()
                        },
                    text = dismissButtonText,
                    fontSize = font_size_16_sp,
                    fontFamily = redHatDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }
    )
}
