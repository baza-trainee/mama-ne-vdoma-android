package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.success

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun UserCreateSuccessScreen(
    modifier: Modifier = Modifier,
    name: String = "",
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { onBack() }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = "Реєстрація пройшла успішно",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = "Вітаємо, $name!",
                fontSize = 20.sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = "Давайте знайдемо для вас найкращу групу.",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = "Щоб зробити це ми задамо вам декілька запитань. Це займе лише кілька хвилин.",
                fontSize = 11.sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onNext
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }
    }
}

@Composable
@Preview
fun UserCreateSuccessPreview() {
    UserCreateSuccessScreen()
}
