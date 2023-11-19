package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.VerifyEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.EditCredentialsSuccessDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun VerifyNewEmailScreen(
    screenState: VerifyEmailViewState,
    uiState: State<VerifyEmailUiState>,
    handleEvent: (VerifyEmailEvent) -> Unit
) {
    BackHandler { handleEvent(VerifyEmailEvent.OnBack) }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    when (val state = uiState.value) {
        VerifyEmailUiState.Idle -> Unit
        is VerifyEmailUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(VerifyEmailEvent.ResetUiState)
        }

        VerifyEmailUiState.OnPasswordChanged -> {
            title ="Пароль змінено успішно!"
            showSuccessDialog = true
            handleEvent(VerifyEmailEvent.ResetUiState)
        }
        VerifyEmailUiState.OnEmailChanged -> {
            title ="Email змінено успішно!"
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
        uiState = remember { mutableStateOf(VerifyEmailUiState.Idle) },
        handleEvent = {}
    )
}