package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.VerifyEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun VerifyNewEmailScreen(
    modifier: Modifier = Modifier,
    screenState: State<VerifyEmailViewState> = mutableStateOf(VerifyEmailViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (VerifyEmailEvent) -> Unit = {}
) {
    BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(VerifyEmailEvent.ResetUiState)
        }
    }

    VerifyEmail(
        otp = screenState.value.otp,
        isOtpValid = screenState.value.otpValid != ValidField.INVALID,
        onVerify = { otp, isFilled -> handleEvent(VerifyEmailEvent.VerifyEmail(otp, isFilled))}
    )

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun VerifyNewEmailScreenPreview() {
    VerifyNewEmailScreen()
}