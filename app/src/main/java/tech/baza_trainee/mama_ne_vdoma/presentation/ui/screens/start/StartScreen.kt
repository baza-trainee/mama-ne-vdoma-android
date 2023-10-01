package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun Start(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            val (image, btnStart, btnLogin) = createRefs()

            Image(
                modifier = modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 24.dp)
                        bottom.linkTo(btnStart.top, margin = 24.dp)
                        height = Dimension.fillToConstraints
                    },
                painter = painterResource(id = R.drawable.collage),
                contentDescription = "start",
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Fit
            )

            Button(
                modifier = modifier
                    .constrainAs(btnStart) {
                        bottom.linkTo(btnLogin.top)
                    }
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onStart
            ) {
                Text(
                    text = "Почати",
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            Text(
                text = getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                modifier = modifier
                    .constrainAs(btnLogin) {
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onLogin()
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }
    }
}

@Composable
@Preview
fun StartScreenPreview() {
    Start()
}