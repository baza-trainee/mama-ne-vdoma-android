package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.VerifyEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun VerifyEmailScreen(
    screenState: VerifyEmailViewState,
    uiState: State<RequestState>,
    title: String,
    handleEvent: (VerifyEmailEvent) -> Unit
) {
    SurfaceWithSystemBars {
        val context = LocalContext.current

        BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(state.error)
                handleEvent(VerifyEmailEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (title.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            VerifyEmail(
                otp = screenState.otp,
                isOtpValid = screenState.otpValid != ValidField.INVALID,
                onVerify = { value, otpInputFilled ->
                    handleEvent(VerifyEmailEvent.Verify(value, otpInputFilled))
                },
                onResend = { handleEvent(VerifyEmailEvent.ResendCode) }
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CodeVerificationPreview() {
    VerifyEmailScreen(
        screenState = VerifyEmailViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        title = "Title",
        handleEvent = { _ -> }
    )
}