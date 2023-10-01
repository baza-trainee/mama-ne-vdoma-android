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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: NewPasswordScreenViewModel,
    onRestore: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val screenState = viewModel.viewState.collectAsStateWithLifecycle()

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
                    password = viewModel.password,
                    onValueChange = { viewModel.validatePassword(it) },
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
                    password = viewModel.confirmPassword,
                    onValueChange = { viewModel.validateConfirmPassword(it) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають"
                )
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onRestore,
                enabled = screenState.value.passwordValid == ValidField.VALID &&
                        screenState.value.confirmPasswordValid == ValidField.VALID
            ) {
                ButtonText(
                    text = "Зберегти пароль"
                )
            }
        }
    }
}

@Composable
@Preview
fun NewPasswordPreview() {
    NewPasswordScreen(
        viewModel = getViewModel()
    )
}