package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.VerifyEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.EditCredentialsSuccessDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun VerifyNewEmailScreen(
    screenState: VerifyEmailViewState,
    uiState: VerifyEmailUiState,
    handleEvent: (VerifyEmailEvent) -> Unit
) {
    BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    when (uiState) {
        VerifyEmailUiState.Idle -> Unit
        is VerifyEmailUiState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(VerifyEmailEvent.ResetUiState)
        }

        VerifyEmailUiState.OnPasswordChanged -> {
            title = stringResource(id = R.string.password_changed)
            showSuccessDialog = true
            handleEvent(VerifyEmailEvent.ResetUiState)
        }
        VerifyEmailUiState.OnEmailChanged -> {
            title = stringResource(id = R.string.email_changed)
            showSuccessDialog = true
            handleEvent(VerifyEmailEvent.ResetUiState)
        }
    }

    VerifyEmail(
        otp = screenState.otp,
        isOtpValid = screenState.otpValid != ValidField.INVALID,
        onVerify = { otp, isFilled -> handleEvent(VerifyEmailEvent.Verify(otp, isFilled)) },
        onResend = {}
    )

    if (showSuccessDialog) {
        EditCredentialsSuccessDialog(
            title = title,
            onDismissRequest = {
                showSuccessDialog = false
                handleEvent(VerifyEmailEvent.GoToMain)
            }
        )
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun VerifyNewEmailScreenPreview() {
    VerifyNewEmailScreen(
        screenState = VerifyEmailViewState(),
        uiState = VerifyEmailUiState.Idle,
        handleEvent = {}
    )
}