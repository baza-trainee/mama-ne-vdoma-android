package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
@Preview
fun SocialLoginBlock(
    modifier: Modifier = Modifier,
    textForBottomButton: @Composable () -> CharSequence = { "" },
    onGoogleLogin: () -> Unit = {},
//    onFBLogin: () -> Unit = {},
    onAction: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = onGoogleLogin,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(id = R.string.continue_with_google))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
//
//        OutlinedButton(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            onClick = onGoogleLogin,
//            colors = ButtonDefaults.elevatedButtonColors(
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onBackground
//            ),
//            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_fb),
//                    contentDescription = "Google",
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
//                Text(text = "Продовжити з Facebook")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))

        if (textForBottomButton() is AnnotatedString)
            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onAction()
                    }
                    .fillMaxWidth()
                    .height(48.dp),
                text = textForBottomButton() as AnnotatedString,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        else
            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onAction()
                    }
                    .fillMaxWidth()
                    .height(48.dp),
                text = textForBottomButton.toString(),
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
    }
}