package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun RestoreSuccessFunc(
    goToMain: () -> Unit
) {
    RestoreSuccess(
        goToMain = goToMain
    )
}

@Composable
fun RestoreSuccess(
    modifier: Modifier = Modifier,
    goToMain: () -> Unit
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxWidth()
        ) {
            val (title, image, btnMain) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Пароль збережено",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily
                )
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 8.dp),
                    text = "Ви успішно змінили свій пароль. " +
                            "Будь ласка, використовуйте цей новий пароль" +
                            "при вході в додаток",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            Image(
                modifier = modifier
                    .constrainAs(image) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(btnMain.top, 64.dp)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.restore_pass_end),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )

            Button(
                modifier = modifier
                    .constrainAs(btnMain) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = goToMain
            ) {
                ButtonText(
                    text = "На головну сторінку"
                )
            }
        }
    }
}

@Composable
@Preview
fun RestoreSuccessPreview() {
    RestoreSuccess(
        goToMain = {}
    )
}
