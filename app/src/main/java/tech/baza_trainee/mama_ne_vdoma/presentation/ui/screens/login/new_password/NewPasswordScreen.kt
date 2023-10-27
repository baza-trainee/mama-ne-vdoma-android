package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    screenState: State<NewPasswordViewState> = mutableStateOf(NewPasswordViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (NewPasswordEvent) -> Unit = { _ -> }
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(NewPasswordEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(NewPasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Відновлення паролю",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Будь ласка, створіть новий пароль нижче",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    password = screenState.value.password,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidatePassword(it)) },
                    isError = screenState.value.passwordValid == ValidField.INVALID
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Повторіть ваш пароль",
                    password = screenState.value.confirmPassword,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidateConfirmPassword(it)) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають"
                )
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(NewPasswordEvent.ResetPassword) },
                enabled = screenState.value.passwordValid == ValidField.VALID &&
                        screenState.value.confirmPasswordValid == ValidField.VALID
            ) {
                ButtonText(
                    text = "Зберегти пароль"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun NewPasswordPreview() {
    NewPasswordScreen()
}