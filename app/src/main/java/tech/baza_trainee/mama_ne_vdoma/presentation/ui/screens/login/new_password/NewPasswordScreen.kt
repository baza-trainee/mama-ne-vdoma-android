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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.CommonUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    screenState: State<NewPasswordViewState> = mutableStateOf(NewPasswordViewState()),
    uiState: State<CommonUiState> = mutableStateOf(CommonUiState.Idle),
    handleEvent: (NewPasswordEvent) -> Unit = { _ -> },
    onRestore: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    SurfaceWithSystemBars(
        modifier = modifier
    ) {
        BackHandler { onBack() }

        val context = LocalContext.current

        when(val state = uiState.value) {
            CommonUiState.Idle -> Unit
            is CommonUiState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(NewPasswordEvent.ResetUiState)
            }
            CommonUiState.OnNext -> {
                onRestore()
                handleEvent(NewPasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Відновлення паролю",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(4.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Будь ласка, створіть новий пароль нижче",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    password = screenState.value.password,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidatePassword(it)) },
                    isError = screenState.value.passwordValid == ValidField.INVALID
                )

                Spacer(modifier = modifier.height(8.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    label = "Повторіть ваш пароль",
                    password = screenState.value.confirmPassword,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidateConfirmPassword(it)) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають"
                )
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
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