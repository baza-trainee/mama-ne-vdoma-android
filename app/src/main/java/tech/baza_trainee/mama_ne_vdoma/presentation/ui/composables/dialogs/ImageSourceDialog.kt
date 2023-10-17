package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceDialog(
    onPickFromGallery: () -> Unit,
    onPickFromCamera: () -> Unit,
    onHideDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onHideDialog
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
                    .clickable { onPickFromGallery() },
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
                    .clickable { onPickFromGallery() },
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