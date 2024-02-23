package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import tech.baza_trainee.mama_ne_vdoma.BuildConfig
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.PrivacyPolicyBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.beginSignInGoogleOneTap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun UserCreateScreen(
    oneTapClient: SignInClient?,
    screenState: UserCreateViewState,
    uiState: State<RequestState>,
    handleEvent: (UserCreateEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(UserCreateEvent.OnBack) }

        val context = LocalContext.current

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(state.error)
                handleEvent(UserCreateEvent.ResetUiState)
            }
        }

        var googleLogin by remember { mutableStateOf(false) }

        val intentSender =
            rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                try {
                    val credential = oneTapClient?.getSignInCredentialFromIntent(it.data)
                    handleEvent(UserCreateEvent.OnGoogleLogin(credential?.googleIdToken.orEmpty()))
                } catch (exc: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.no_data_for_auth),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        LaunchedEffect(key1 = googleLogin) {
            if (googleLogin) {
                try {
                    val signUpRequest = BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(
                            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(BuildConfig.ONE_TAP_SERVER_CLIENT_ID)
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                        )
                        .build()

                    val activity = context.findActivity()
                    val result = activity.beginSignInGoogleOneTap(oneTapClient, signUpRequest)
                    intentSender.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                            .build()
                    )
                } catch (exc: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.auth_impossible),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            googleLogin = false
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = size_16_dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                text = stringResource(id = R.string.title_create_user_profile),
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
                onValueChange = { handleEvent(UserCreateEvent.ValidateEmail(it)) },
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
                onValueChange = { handleEvent(UserCreateEvent.ValidatePassword(it)) },
                isError = screenState.passwordValid == ValidField.INVALID,
                imeAction = ImeAction.Next
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_2_dp),
                text = stringResource(id = R.string.password_rule_hint),
                fontSize = font_size_14_sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = font_size_18_sp
            )

            PasswordTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                label = stringResource(id = R.string.repeat_password),
                password = screenState.confirmPassword,
                onValueChange = { handleEvent(UserCreateEvent.ValidateConfirmPassword(it)) },
                isError = screenState.confirmPasswordValid == ValidField.INVALID,
                errorText = stringResource(id = R.string.passwords_do_not_match),
                imeAction = ImeAction.Done,
                onImeActionPerformed = { handleEvent(UserCreateEvent.RegisterUser) }
            )

            PrivacyPolicyBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_8_dp),
                isChecked = screenState.isPolicyChecked,
                onCheckedChanged = { handleEvent(UserCreateEvent.UpdatePolicyCheck(it)) }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_24_dp)
                    .height(size_48_dp),
                onClick = {
                    handleEvent(UserCreateEvent.RegisterUser)
                },
                enabled = screenState.isAllConform
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_sign_up)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SocialLoginBlock(
                modifier = Modifier.fillMaxWidth(),
                textForBottomButton = {
                    getTextWithUnderline(
                        stringResource(id = R.string.account_existed),
                        stringResource(id = R.string.action_log_in)
                    )
                },
                onGoogleLogin = { googleLogin = true },
                onAction = { handleEvent(UserCreateEvent.OnLogin) }
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun UserCreatePreview() {
    UserCreateScreen(
        oneTapClient = null,
        screenState = UserCreateViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = { _ -> }
    )
}