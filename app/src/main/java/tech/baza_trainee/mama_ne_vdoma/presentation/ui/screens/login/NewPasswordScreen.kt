package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.NewPasswordViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun NewPasswordFunc(
    viewModel: NewPasswordScreenViewModel,
    onRestore: () -> Unit
) {
    NewPassword(
        screenState = viewModel.viewState.collectAsStateWithLifecycle(),
        validatePassword = { viewModel.validatePassword(it) },
        validateConfirmPassword = { viewModel.validateConfirmPassword(it) },
        onRestore = onRestore
    )
}

@Composable
fun NewPassword(
    modifier: Modifier = Modifier,
    screenState: State<NewPasswordViewState> = mutableStateOf(NewPasswordViewState()),
    validatePassword: (String) -> Unit = {},
    validateConfirmPassword: (String) -> Unit = {},
    onRestore: () -> Unit = {}
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .fillMaxSize()
        ) {
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
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = modifier.height(4.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Будь ласка, створіть новий пароль нижче",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    PasswordTextFieldWithError(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        password = screenState.value.password,
                        onValueChange = { validatePassword(it) },
                        isError = screenState.value.passwordValid == ValidField.INVALID
                    )

                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Пароль має бути від 6 до 24 символів, обов’язково містити латинські букви, цифри, спеціальні знаки",
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    PasswordTextFieldWithError(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        label = "Повторіть ваш пароль",
                        password = screenState.value.confirmPassword,
                        onValueChange = { validateConfirmPassword(it) },
                        isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                        errorText = "Паролі не співпадають"
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp)
                        .height(48.dp),
                    onClick = onRestore,
                    enabled = screenState.value.passwordValid == ValidField.VALID &&
                            screenState.value.confirmPasswordValid == ValidField.VALID
                ) {
                    Text(text = "Зберегти пароль")
                }
            }
        }
    }
}

@Composable
@Preview
fun NewPasswordPreview() {
    NewPassword()
}