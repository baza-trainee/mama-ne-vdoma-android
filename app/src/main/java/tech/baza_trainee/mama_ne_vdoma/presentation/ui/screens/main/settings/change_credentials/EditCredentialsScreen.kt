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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun EditCredentialsScreen(
    modifier: Modifier = Modifier,
    screenState: State<EditCredentialsViewState> = mutableStateOf(EditCredentialsViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (EditCredentialsEvent) -> Unit = {}
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
            value = screenState.value.email,
            label = "Введіть свій email",
            onValueChange = { handleEvent(EditCredentialsEvent.ValidateEmail(it)) },
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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { handleEvent(EditCredentialsEvent.VerifyEmail) }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = { handleEvent(EditCredentialsEvent.VerifyEmail) },
            enabled = screenState.value.emailValid == ValidField.VALID
        ) {
            ButtonText(
                text = "Змінити email"
            )
        }

        Spacer(modifier = modifier.height(32.dp))

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

        Spacer(modifier = modifier.height(32.dp))

        //Password
        PasswordTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            password = screenState.value.password,
            onValueChange = { handleEvent(EditCredentialsEvent.ValidatePassword(it)) },
            isError = screenState.value.passwordValid == ValidField.INVALID,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        PasswordTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            label = "Повторіть ваш пароль",
            password = screenState.value.confirmPassword,
            onValueChange = { handleEvent(EditCredentialsEvent.ValidateConfirmPassword(it)) },
            isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
            errorText = "Паролі не співпадають",
            imeAction = ImeAction.Done,
            onImeActionPerformed = { handleEvent(EditCredentialsEvent.ResetPassword) }
        )

        Button(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = { handleEvent(EditCredentialsEvent.ResetPassword) },
            enabled = screenState.value.passwordValid == ValidField.VALID &&
                    screenState.value.confirmPasswordValid == ValidField.VALID
        ) {
            ButtonText(
                text = "Змінити пароль"
            )
        }
    }
}

@Composable
@Preview
fun EditCredentialsScreenPreview() {
    EditCredentialsScreen()
}