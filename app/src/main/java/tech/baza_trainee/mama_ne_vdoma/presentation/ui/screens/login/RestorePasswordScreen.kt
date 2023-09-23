package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme

@Composable
fun RestorePasswordFunc(
    onBack: () -> Unit,
    onRestore: () -> Unit
) {
    RestorePassword(onBack = onBack, onRestore = onRestore)
}

@Composable
fun RestorePassword(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
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
                        Text(
                            modifier = modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    onBack()
                                },
                            text = "<",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            text = "Забули пароль?",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
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
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        label = { Text("Введіть свій email") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Gray,
                            unfocusedContainerColor = Gray,
                            disabledContainerColor = Gray,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                        )
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 24.dp),
                    onClick = onRestore
                ) {
                    Text(text = "Відправити")
                }
            }
        }
    }
}

@Composable
@Preview
fun RestorePasswordPreview() {
    RestorePassword()
}