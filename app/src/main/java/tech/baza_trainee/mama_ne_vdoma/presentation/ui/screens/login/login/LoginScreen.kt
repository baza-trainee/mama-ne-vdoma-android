package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialCancellationException
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_64_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.AuthCredential
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.requestAuthCredential
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun LoginUserScreen(
    credentialManager: CredentialManager?,
    screenState: LoginViewState,
    uiState: RequestState,
    handleEvent: (LoginEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(LoginEvent.OnBack) }

        val context = LocalContext.current
        val resources = LocalResources.current

        when (uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(LoginEvent.ResetUiState)
            }
        }

        var googleLogin by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = googleLogin) {
            if (googleLogin) {
                runCatching {
                    context.findActivity().requestAuthCredential(
                        credentialManager = credentialManager,
                        includePassword = true,
                        autoSelectEnabled = true,
                        filterByAuthorizedAccounts = true
                    )
                }.onSuccess { credential ->
                    when (credential) {
                        is AuthCredential.GoogleIdToken ->
                            handleEvent(LoginEvent.LoginWithToken(credential.token))

                        is AuthCredential.Password -> handleEvent(
                            LoginEvent.LoginWithPassword(
                                credential.username,
                                credential.password
                            )
                        )

                        null -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.no_data_for_auth),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.onFailure { exc ->
                    // dismissing the credential sheet is not an error
                    if (exc !is GetCredentialCancellationException) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.auth_impossible),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                googleLogin = false
            }
        }

        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = size_16_dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                text = stringResource(id = R.string.profile_log_in),
                fontSize = font_size_20_sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = redHatDisplayFontFamily
            )

            OutlinedTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_24_dp),
                value = screenState.email,
                hint = stringResource(id = R.string.email),
                label = stringResource(id = R.string.enter_your_email),
                onValueChange = { handleEvent(LoginEvent.ValidateEmail(it)) },
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
                    imeAction = ImeAction.Next
                )
            )

            PasswordTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                password = screenState.password,
                onValueChange = { handleEvent(LoginEvent.ValidatePassword(it)) },
                isError = screenState.passwordValid == ValidField.INVALID,
                errorText = stringResource(id = R.string.incorrect_password),
                imeAction = ImeAction.Done,
                onImeActionPerformed = { handleEvent(LoginEvent.LoginUser) },
            )

            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = size_8_dp)
                    .align(alignment = Alignment.End)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleEvent(LoginEvent.OnRestore) },
                text = getTextWithUnderline(
                    "",
                    stringResource(id = R.string.forgot_password),
                    false
                ),
                textAlign = TextAlign.End,
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily
            )

            Button(
                modifier = Modifier
                    .padding(bottom = size_16_dp, top = size_64_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(LoginEvent.LoginUser) },
                enabled = screenState.passwordValid == ValidField.VALID &&
                        screenState.emailValid == ValidField.VALID
            ) {
                Text(
                    text = stringResource(id = R.string.action_log_in),
                    fontFamily = redHatDisplayFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SocialLoginBlock(
                modifier = Modifier.fillMaxWidth(),
                textForBottomButton = {
                    getTextWithUnderline(
                        stringResource(id = R.string.do_not_have_account),
                        stringResource(id = R.string.action_sign_up)
                    )
                },
                onGoogleLogin = { googleLogin = true },
                onAction = { handleEvent(LoginEvent.OnCreate) }
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun LoginUserPreview() {
    LoginUserScreen(
        credentialManager = null,
        screenState = LoginViewState(),
        uiState = RequestState.Idle,
        handleEvent = { _ -> }
    )
}
