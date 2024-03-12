package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.VerifyEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun VerifyEmailScreen(
    screenState: VerifyEmailViewState,
    uiState: RequestState,
    @StringRes title: Int = -1,
    handleEvent: (VerifyEmailEvent) -> Unit
) {
    SurfaceWithSystemBars {
        val context = LocalContext.current

        BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

        when (uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(VerifyEmailEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = size_16_dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (title != -1) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_16_dp),
                    text = stringResource(id = title),
                    fontSize = font_size_20_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            VerifyEmail(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_48_dp),
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
        uiState = RequestState.Idle,
        handleEvent = { _ -> }
    )
}