package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OtpTextField
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun VerifyEmailScreen(
    modifier: Modifier = Modifier,
    screenState: State<VerifyEmailViewState> = mutableStateOf(VerifyEmailViewState()),
    uiState: State<VerifyEmailUiState> = mutableStateOf(VerifyEmailUiState.Idle),
    title: String = "0",
    handleEvent: (VerifyEmailEvent) -> Unit = { _ -> }
) {
    BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

    SurfaceWithSystemBars {
        val context = LocalContext.current

        when(val state = uiState.value) {
            VerifyEmailUiState.Idle -> Unit
            is VerifyEmailUiState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(VerifyEmailEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .imePadding()
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = title,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Ми відправили на вашу пошту електронний лист з кодом із 4 символів." +
                            " Перевірте пошту, і якщо не знайдете листа — теку «спам»",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(32.dp))

                OtpTextField(
                    otpText = screenState.value.otp,
                    onOtpTextChange = { value, otpInputFilled ->
                        handleEvent(VerifyEmailEvent.VerifyEmail(value, otpInputFilled))
                    }
                )
                if (screenState.value.otpValid == ValidField.INVALID)
                    Text(
                        text = "Ви ввели невірний код",
                        color = Color.Red,
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        fontFamily = redHatDisplayFontFamily,
                        style = TextStyle(
                            fontFamily = redHatDisplayFontFamily
                        ),
                        fontSize = 14.sp
                    )
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {}
            ) {
                ButtonText(
                    text = "Надіслати код ще раз"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CodeVerificationPreview() {
    VerifyEmailScreen()
}