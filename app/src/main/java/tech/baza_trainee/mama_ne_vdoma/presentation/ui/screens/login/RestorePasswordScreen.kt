package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.palm.composestateevents.EventEffect
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.RestorePasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.RestorePasswordViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun RestorePasswordScreen(
    modifier: Modifier = Modifier,
    screenState: State<RestorePasswordViewState> = mutableStateOf(RestorePasswordViewState()),
    onHandleEvent: (RestorePasswordEvent) -> Unit = { _ -> },
    onBack: () -> Unit = {},
    onRestore: () -> Unit = {}
) {
    SurfaceWithSystemBars(
        modifier = modifier
    ) {
        val context = LocalContext.current

        EventEffect(
            event = screenState.value.loginSuccess,
            onConsumed = {}
        ) {
            onHandleEvent(RestorePasswordEvent.OnSuccess)
            onRestore()
        }

        EventEffect(
            event = screenState.value.requestError,
            onConsumed = { onHandleEvent(RestorePasswordEvent.ConsumeRequestError) }
        ) { if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

        Column(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = modifier.height(16.dp))

                Row(
                    modifier = modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                ) {
                    IconButton(
                        modifier = modifier
                            .padding(start = 16.dp)
                            .height(24.dp)
                            .width(24.dp),
                        onClick = { onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        modifier = modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, end = 24.dp),
                        text = "Забули пароль?",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = modifier.height(8.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    text = "Не турбуйтеся! Будь ласка, введіть " +
                            "свій email за яким ви реєструвались, " +
                            "щоб отримати лист з інструкціями",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(24.dp))

                OutlinedTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = screenState.value.email,
                    label = "Введіть свій email",
                    onValueChange = { onHandleEvent(RestorePasswordEvent.ValidateEmail(it)) },
                    isError = screenState.value.emailValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректний email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    }
                )
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onRestore,
                enabled = screenState.value.emailValid == ValidField.VALID
            ) {
                Text(
                    text = "Відправити",
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }
    }
}

@Composable
@Preview
fun RestorePasswordPreview() {
    RestorePasswordScreen()
}