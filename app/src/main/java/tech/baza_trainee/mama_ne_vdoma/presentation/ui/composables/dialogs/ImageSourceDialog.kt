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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ImageSourceDialog(
    onPickFromGallery: () -> Unit = {},
    onPickFromCamera: () -> Unit = {},
    onHideDialog: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onHideDialog
    ) {
        val dialogModifier = Modifier
        Column(
            modifier = dialogModifier
                .clip(RoundedCornerShape(size_8_dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(size_16_dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = dialogModifier
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.choose_image),
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                fontFamily = redHatDisplayFontFamily
            )
            Text(
                modifier = dialogModifier
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.choose_source),
                fontSize = font_size_14_sp,
                textAlign = TextAlign.Start,
                fontFamily = redHatDisplayFontFamily
            )
            Row(
                modifier = dialogModifier
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth()
                    .clickable { onPickFromCamera() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null
                )
                Text(
                    modifier = dialogModifier
                        .padding(start = size_8_dp)
                        .weight(1f)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.choose_camera),
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Start,
                    fontFamily = redHatDisplayFontFamily
                )
            }
            HorizontalDivider(
                modifier = dialogModifier
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_16_dp)
                    .fillMaxWidth(),
                thickness = size_2_dp,
                color = SlateGray
            )
            Row(
                modifier = dialogModifier
                    .padding(top = size_16_dp, bottom = size_16_dp)
                    .padding(horizontal = size_16_dp)
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
                        .padding(start = size_8_dp)
                        .weight(1f)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.choose_gallery),
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Start,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }
    }
}