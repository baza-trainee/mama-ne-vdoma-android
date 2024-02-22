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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun NewPasswordScreen(
    screenState: NewPasswordViewState,
    uiState: State<RequestState>,
    handleEvent: (NewPasswordEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(NewPasswordEvent.OnBack) }

        val context = LocalContext.current

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(state.error)
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
                    text = stringResource(id = R.string.title_restore_password),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    text = stringResource(id = R.string.create_new_password),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                PasswordTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    password = screenState.password,
                    onValueChange = { handleEvent(NewPasswordEvent.ValidatePassword(it)) },
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
                        .padding(top = 16.dp),
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
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
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
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = { _ -> }
    )
}