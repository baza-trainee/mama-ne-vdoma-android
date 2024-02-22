package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials

import android.widget.Toast
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun EditCredentialsScreen(
    screenState: EditCredentialsViewState,
    uiState: State<RequestState>,
    handleEvent: (EditCredentialsEvent) -> Unit
) {
    BackHandler { handleEvent(EditCredentialsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
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
        Spacer(modifier = Modifier.height(8.dp))

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
                .padding(top = 24.dp)
                .height(48.dp),
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
                .padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringResource(id = R.string.or),
                fontSize = 14.sp,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height = 2.dp)
                    .background(color = SlateGray)
            )
        }

        //Password
        PasswordTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            password = screenState.password,
            onValueChange = { handleEvent(EditCredentialsEvent.ValidatePassword(it)) },
            isError = screenState.passwordValid == ValidField.INVALID,
            imeAction = ImeAction.Next
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(id = R.string.password_rule_hint),
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily,
            lineHeight = 18.sp
        )

        PasswordTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
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
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
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
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}