package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import tech.baza_trainee.mama_ne_vdoma.BuildConfig
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.PrivacyPolicyBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.beginSignInGoogleOneTap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun UserCreateScreen(
    modifier: Modifier = Modifier,
    oneTapClient: SignInClient? = null,
    screenState: State<UserCreateViewState> = mutableStateOf(UserCreateViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (UserCreateEvent) -> Unit = { _ -> }
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
                } catch (exc: ApiException) {
                    Toast.makeText(context, "Немає даних для авторизації", Toast.LENGTH_LONG)
                        .show()
                }
            }

        LaunchedEffect(key1 = googleLogin) {
            if (googleLogin) {
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
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Створити профіль",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    value = screenState.value.email,
                    hint = "Email",
                    label = "Введіть свій email",
                    onValueChange = { handleEvent(UserCreateEvent.ValidateEmail(it)) },
                    isError = screenState.value.emailValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректний email",
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

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    password = screenState.value.password,
                    onValueChange = { handleEvent(UserCreateEvent.ValidatePassword(it)) },
                    isError = screenState.value.passwordValid == ValidField.INVALID,
                    imeAction = ImeAction.Next
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text =
                    "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Повторіть свій пароль",
                    password = screenState.value.confirmPassword,
                    onValueChange = { handleEvent(UserCreateEvent.ValidateConfirmPassword(it)) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають",
                    imeAction = ImeAction.Done,
                    onImeActionPerformed = { handleEvent(UserCreateEvent.RegisterUser) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PrivacyPolicyBlock(
                    modifier = Modifier.fillMaxWidth(),
                    isChecked = screenState.value.isPolicyChecked,
                    onCheckedChanged = { handleEvent(UserCreateEvent.UpdatePolicyCheck(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        handleEvent(UserCreateEvent.RegisterUser)
                    },
                    enabled = screenState.value.isAllConform
                ) {
                    ButtonText(
                        text = "Зареєструватись"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .height(height = 2.dp)
                            .background(color = SlateGray)
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "чи",
                        fontSize = 14.sp,
                    )
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .height(height = 2.dp)
                            .background(color = SlateGray)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            SocialLoginBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textForBottomButton = getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                onGoogleLogin = { googleLogin = true },
                onAction = { handleEvent(UserCreateEvent.OnLogin) }
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun UserCreatePreview() {
    UserCreateScreen()
}