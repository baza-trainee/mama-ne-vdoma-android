package com.example.mama_ne_vdoma.ui.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mama_ne_vdoma.R
import com.example.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import com.example.mama_ne_vdoma.utils.CustomButton
import com.example.mama_ne_vdoma.utils.getTextWithUnderline

@Composable
fun StartScreenFunc(
    onStart: () -> Unit,
    onLogin: () -> Unit
) {
    StartScreen(onStart = onStart, onLogin = onLogin)
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = modifier.fillMaxWidth()
            ) {
                val (image, btnStart, btnLogin) = createRefs()
                Image(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(btnStart.top, margin = 16.dp)
                            height = Dimension.fillToConstraints
                        },
                    painter = painterResource(id = R.drawable.collage),
                    contentDescription = "start",
                    contentScale = ContentScale.Fit
                )
                CustomButton(
                    modifier = modifier
                        .constrainAs(btnStart) {
                            bottom.linkTo(btnLogin.top, margin = 16.dp)
                        }
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    text = "Почати",
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    action = onStart
                )
                CustomButton(
                    modifier = modifier.constrainAs(btnLogin) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    text = getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    action = onLogin
                )
            }
        }
    }
}

@Composable
@Preview
fun StartScreenPreview() {
    StartScreen()
}