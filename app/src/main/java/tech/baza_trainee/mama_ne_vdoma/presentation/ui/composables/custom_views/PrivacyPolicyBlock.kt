package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp

@Composable
@Preview
fun PrivacyPolicyBlock(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChanged
        )
        Column(
            modifier = Modifier.fillMaxWidth(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(id = R.string.user_privacy_agreement),
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = font_size_18_sp
            )

            Spacer(modifier = Modifier.height(size_4_dp))

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clickable {
                        val uri = "https://mama-ne-vdoma.online/privacy"
                        uriHandler.openUri(uri)
                    },
                text = getTextWithUnderline(
                    simpleText = stringResource(id = R.string.list_tab),
                    underlinedText = stringResource(id = R.string.privacy_policy),
                    isBold = true
                ),
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clickable {
                        val uri = "https://mama-ne-vdoma.online/terms"
                        uriHandler.openUri(uri)
                    },
                text = getTextWithUnderline(
                    simpleText = stringResource(id = R.string.list_tab),
                    underlinedText = stringResource(id = R.string.user_agreement),
                    isBold = true
                ),
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clickable {
                        val uri = "https://mama-ne-vdoma.online/refusal"
                        uriHandler.openUri(uri)
                    },
                text = getTextWithUnderline(
                    simpleText = stringResource(id = R.string.list_tab),
                    underlinedText = stringResource(id = R.string.responsibility_refusal),
                    isBold = true
                ),
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily
            )
        }
    }
}