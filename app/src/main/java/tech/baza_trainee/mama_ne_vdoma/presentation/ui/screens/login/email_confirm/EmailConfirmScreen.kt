package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.email_confirm

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun EmailConfirmScreen(
    uiState: RequestState,
    email: String,
    password: String,
    handleEvent: (RestorePasswordEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.email_sent),
                info = stringResource(id = R.string.check_your_mailbox, email)
            )
        }
    ) { paddingValues ->

        val context = LocalContext.current

        when (uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(RestorePasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
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
                    .padding(all = size_16_dp)
                    .height(size_48_dp),
                onClick = { handleEvent(RestorePasswordEvent.OnLogin(email, password)) }
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_log_in)
                )
            }

            Text(
                text = getTextWithUnderline(
                    stringResource(id = R.string.did_not_get_email),
                    stringResource(id = R.string.action_send_again)
                ),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleEvent(RestorePasswordEvent.SendEmail) }
                    .fillMaxWidth()
                    .padding(horizontal = size_24_dp, vertical = size_16_dp),
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }
    }
}

@Composable
@Preview
fun EmailConfirmPreview() {
    EmailConfirmScreen(
        uiState = RequestState.Idle,
        email = "",
        password = "",
        handleEvent = { _ -> }
    )
}
