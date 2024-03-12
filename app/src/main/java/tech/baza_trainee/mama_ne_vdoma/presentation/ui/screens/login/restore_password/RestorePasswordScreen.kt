package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun RestorePasswordScreen(
    screenState: RestorePasswordViewState,
    uiState: RequestState,
    handleEvent: (RestorePasswordEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler {
            handleEvent(RestorePasswordEvent.OnBack)
        }

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
                .imePadding()
                .fillMaxWidth()
                .padding(bottom = size_16_dp)
                .padding(horizontal = size_16_dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(top = size_16_dp)
                ) {
                    IconButton(
                        modifier = Modifier
                            .height(size_24_dp)
                            .width(size_24_dp),
                        onClick = { handleEvent(RestorePasswordEvent.OnBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.forgot_password),
                        fontSize = font_size_20_sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_16_dp),
                    text = stringResource(id = R.string.email_restore_instructions),
                    fontSize = font_size_14_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_48_dp),
                    value = screenState.email,
                    hint = stringResource(id = R.string.email),
                    label = stringResource(id = R.string.enter_your_email),
                    onValueChange = { handleEvent(RestorePasswordEvent.ValidateEmail(it)) },
                    isError = screenState.emailValid == ValidField.INVALID,
                    errorText = stringResource(id = R.string.incorrect_email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { handleEvent(RestorePasswordEvent.SendEmail) },
                    )
                )
            }

            Button(
                modifier = Modifier
                    .padding(vertical = size_16_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(RestorePasswordEvent.SendEmail) },
                enabled = screenState.emailValid == ValidField.VALID
            ) {
                Text(
                    text = stringResource(id = R.string.action_send),
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun RestorePasswordPreview() {
    RestorePasswordScreen(
        screenState = RestorePasswordViewState(),
        uiState = RequestState.Idle,
        handleEvent = { _ -> }
    )
}