package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun SocialLoginBlock(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    textForBottomButton: CharSequence = "",
//    onGoogleLogin: () -> Unit = {},
//    onFBLogin: () -> Unit = {},
    onAction: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .wrapContentHeight()
    ) {
//        OutlinedButton(
//            modifier = modifier
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
//                modifier = modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_google),
//                    contentDescription = "Google",
//                    modifier = modifier.size(ButtonDefaults.IconSize)
//                )
//                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
//                Text(text = "Продовжити з Google")
//            }
//        }
//
//        Spacer(modifier = modifier.height(24.dp))
//
//        OutlinedButton(
//            modifier = modifier
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
//                modifier = modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_fb),
//                    contentDescription = "Google",
//                    modifier = modifier.size(ButtonDefaults.IconSize)
//                )
//                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
//                Text(text = "Продовжити з Facebook")
//            }
//        }
//
//        Spacer(modifier = modifier.height(24.dp))

        if (textForBottomButton is AnnotatedString)
            Text(
                modifier = modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onAction()
                    }
                    .fillMaxWidth()
                    .height(48.dp),
                text = textForBottomButton,
                textAlign = TextAlign.Center
            )
        else
            Text(
                modifier = modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onAction()
                    }
                    .fillMaxWidth()
                    .height(48.dp),
                text = textForBottomButton.toString(),
                textAlign = TextAlign.Center
            )
    }
}