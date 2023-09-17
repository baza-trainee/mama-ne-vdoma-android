package com.example.mama_ne_vdoma.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mama_ne_vdoma.R

@Composable
@Preview
fun SocialLoginBlock(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    textForBottomButton: CharSequence = "",
    onGoogleLogin: () -> Unit = {},
    onFBLogin: () -> Unit = {},
    onLogin: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .wrapContentHeight()
    ) {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = onGoogleLogin,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Продовжити з Google")
            }
        }

        Spacer(modifier = modifier.height(24.dp))

        OutlinedButton(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = onGoogleLogin,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fb),
                    contentDescription = "Google",
                    modifier = modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Продовжити з Facebook")
            }
        }

        Spacer(modifier = modifier.height(24.dp))

        CustomButton(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            text = textForBottomButton,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            action = onLogin
        )
    }
}