package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity

@Composable
fun Start(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    SurfaceWithSystemBars {

        val activity = LocalContext.current.findActivity()

        BackHandler { activity.finish() }

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (image, btnStart, btnLogin) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
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
                modifier = Modifier
                    .constrainAs(btnStart) {
                        bottom.linkTo(btnLogin.top)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
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
                modifier = Modifier
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
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