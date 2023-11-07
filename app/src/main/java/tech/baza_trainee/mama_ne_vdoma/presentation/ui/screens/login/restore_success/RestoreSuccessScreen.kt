package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_success

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText

@Composable
fun RestoreSuccessScreen(
    modifier: Modifier = Modifier,
    goToMain: () -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { goToMain() }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = "Пароль збережено",
                info = "Ви успішно змінили свій пароль. " +
                        "Будь ласка, використовуйте цей новий пароль " +
                        "при вході в додаток"
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
                painter = painterResource(id = R.drawable.restore_pass_end),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
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
    RestoreSuccessScreen()
}
