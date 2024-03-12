package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun EditCredentialsScreen(
    screenState: EditCredentialsViewState,
    uiState: RequestState,
    handleEvent: (EditCredentialsEvent) -> Unit
) {
    BackHandler { handleEvent(EditCredentialsEvent.OnBack) }

    val context = LocalContext.current

    when (uiState) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(EditCredentialsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(size_8_dp))

        //Login
        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.email,
            hint = "Email",
            label = stringResource(id = R.string.enter_your_email),
            onValueChange = { handleEvent(EditCredentialsEvent.ValidateEmail(it)) },
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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { handleEvent(EditCredentialsEvent.VerifyEmail) }
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_24_dp)
                .height(size_48_dp),
            onClick = { handleEvent(EditCredentialsEvent.VerifyEmail) },
            enabled = screenState.emailValid == ValidField.VALID
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_change_email)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_32_dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = size_2_dp)
                    .background(color = SlateGray)
            )
            Text(
                modifier = Modifier.padding(horizontal = size_4_dp),
                text = stringResource(id = R.string.or),
                fontSize = font_size_14_sp,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = size_2_dp)
                    .background(color = SlateGray)
            )
        }

        //Password
        PasswordTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_32_dp),
            password = screenState.password,
            onValueChange = { handleEvent(EditCredentialsEvent.ValidatePassword(it)) },
            isError = screenState.passwordValid == ValidField.INVALID,
            imeAction = ImeAction.Next
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            text = stringResource(id = R.string.password_rule_hint),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily,
            lineHeight = font_size_18_sp
        )

        PasswordTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_24_dp),
            label = stringResource(id = R.string.repeat_password),
            password = screenState.confirmPassword,
            onValueChange = { handleEvent(EditCredentialsEvent.ValidateConfirmPassword(it)) },
            isError = screenState.confirmPasswordValid == ValidField.INVALID,
            errorText = stringResource(id = R.string.passwords_do_not_match),
            imeAction = ImeAction.Done,
            onImeActionPerformed = { handleEvent(EditCredentialsEvent.ResetPassword) }
        )

        Button(
            modifier = Modifier
                .padding(vertical = size_16_dp)
                .fillMaxWidth()
                .height(size_48_dp),
            onClick = { handleEvent(EditCredentialsEvent.ResetPassword) },
            enabled = screenState.passwordValid == ValidField.VALID &&
                    screenState.confirmPasswordValid == ValidField.VALID
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_change_password)
            )
        }
    }
}

@Composable
@Preview
fun EditCredentialsScreenPreview() {
    EditCredentialsScreen(
        screenState = EditCredentialsViewState(),
        uiState = RequestState.Idle,
        handleEvent = {}
    )
}