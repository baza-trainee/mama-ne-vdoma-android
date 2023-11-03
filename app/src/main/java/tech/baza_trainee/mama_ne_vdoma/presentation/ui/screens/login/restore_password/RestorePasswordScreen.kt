package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun RestorePasswordScreen(
    modifier: Modifier = Modifier,
    screenState: State<RestorePasswordViewState> = mutableStateOf(RestorePasswordViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (RestorePasswordEvent) -> Unit = { _ -> }
) {
    SurfaceWithSystemBars {
        BackHandler {
            handleEvent(RestorePasswordEvent.OnBack)
        }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(RestorePasswordEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                ) {
                    IconButton(
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp),
                        onClick = { handleEvent(RestorePasswordEvent.OnBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        text = "Забули пароль?",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Не турбуйтеся! Будь ласка, введіть " +
                            "свій email за яким ви реєструвались, " +
                            "щоб отримати лист з інструкціями",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(48.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    value = screenState.value.email,
                    label = "Введіть свій email",
                    onValueChange = { handleEvent(RestorePasswordEvent.ValidateEmail(it)) },
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
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { handleEvent(RestorePasswordEvent.SendEmail) },
                    )
                )
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(RestorePasswordEvent.SendEmail) },
                enabled = screenState.value.emailValid == ValidField.VALID
            ) {
                Text(
                    text = "Відправити",
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun RestorePasswordPreview() {
    RestorePasswordScreen()
}