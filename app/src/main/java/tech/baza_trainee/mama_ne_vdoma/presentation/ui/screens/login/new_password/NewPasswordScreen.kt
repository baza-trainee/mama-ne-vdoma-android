package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun NewPasswordScreen(
    screenState: NewPasswordViewState,
    uiState: RequestState,
    handleEvent: (NewPasswordEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(NewPasswordEvent.OnBack) }

        val context = LocalContext.current

        when (uiState) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(uiState.error)
                handleEvent(NewPasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(top = size_16_dp)
                .padding(horizontal = size_16_dp),
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
                    text = stringResource(id = R.string.title_restore_password),
                    fontSize = font_size_20_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_4_dp),
                    text = stringResource(id = R.string.create_new_password),
                    fontSize = font_size_14_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                PasswordTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_16_dp),
                    password = screenState.password,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidatePassword(it)) },
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
                        .padding(top = size_16_dp),
                    label = stringResource(id = R.string.repeat_password),
                    password = screenState.confirmPassword,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidateConfirmPassword(it)) },
                    isError = screenState.confirmPasswordValid == ValidField.INVALID,
                    errorText = stringResource(id = R.string.passwords_do_not_match),
                    imeAction = ImeAction.Done,
                    onImeActionPerformed = { handleEvent(NewPasswordEvent.ResetPassword) }
                )
            }

            Button(
                modifier = Modifier
                    .padding(vertical = size_16_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(NewPasswordEvent.ResetPassword) },
                enabled = screenState.passwordValid == ValidField.VALID &&
                        screenState.confirmPasswordValid == ValidField.VALID
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_save_password)
                )
            }
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun NewPasswordPreview() {
    NewPasswordScreen(
        screenState = NewPasswordViewState(),
        uiState = RequestState.Idle,
        handleEvent = { _ -> }
    )
}