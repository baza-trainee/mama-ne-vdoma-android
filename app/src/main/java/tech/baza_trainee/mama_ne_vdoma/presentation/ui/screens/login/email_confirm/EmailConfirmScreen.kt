package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.email_confirm

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun EmailConfirmScreen(
    modifier: Modifier = Modifier,
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    email: String = "",
    password: String = "",
    handleEvent: (RestorePasswordEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
        val context = LocalContext.current

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(RestorePasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = "Лист був відправлений",
                info = "Перевірте свою пошту $email, " +
                        "щоб отримати подальші інструкції з " +
                        "відновлення паролю"
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
                painter = painterResource(id = R.drawable.email_confirm),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(48.dp),
                onClick = { handleEvent(RestorePasswordEvent.OnLogin(email, password)) }
            ) {
                ButtonText(
                    text = "Увійти"
                )
            }

            Text(
                text = getTextWithUnderline("Не отримали листа? ", "Відправити ще раз"),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleEvent(RestorePasswordEvent.SendEmail) }
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
fun EmailConfirmPreview() {
    EmailConfirmScreen()
}
