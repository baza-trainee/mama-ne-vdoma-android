package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.canopas.campose.countrypicker.CountryPickerBottomSheet
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray

@Composable
fun EnterPhoneFunc(
    onCreateUser: () -> Unit
) {
    EnterPhone(
        onCreateUser = onCreateUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPhone(
    modifier: Modifier = Modifier,
    onCreateUser: () -> Unit
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = modifier
                    .imePadding()
                    .fillMaxWidth()
            ) {
                val (title, input, next) = createRefs()

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(title) {
                            top.linkTo(parent.top, 16.dp)
                        },
                    text = "Заповнення профілю",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                var openBottomSheet by rememberSaveable { mutableStateOf(false) }
                val phoneText = remember { mutableStateOf("") }
                val codeText = remember { mutableStateOf("") }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(input) {
                            top.linkTo(title.bottom, 24.dp)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        modifier = modifier
                            .weight(.25f)
                            .padding(start = 24.dp)
                            .clickable {
                                openBottomSheet = true
                            },
                        value = codeText.value,
                        label = { Text("Код") },
                        onValueChange = {},
                        enabled = false,
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SlateGray,
                            unfocusedContainerColor = SlateGray,
                            disabledContainerColor = SlateGray,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                            disabledBorderColor = MaterialTheme.colorScheme.background
                        ),
                        shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                    )
                    OutlinedTextField(
                        modifier = modifier
                            .weight(.75f)
                            .padding(end = 24.dp),
                        value = phoneText.value,
                        label = { Text("Введіть свій номер телефону") },
                        onValueChange = { phoneText.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Gray,
                            unfocusedContainerColor = Gray,
                            disabledContainerColor = Gray,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                            disabledBorderColor = MaterialTheme.colorScheme.background
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
                }

                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 24.dp)
                        .constrainAs(next) {
                            bottom.linkTo(parent.bottom, 16.dp)
                        },
                    onClick = onCreateUser
                ) {
                    Text(text = "Далі")
                }

                if (openBottomSheet) {
                    CountryPickerBottomSheet(
                        bottomSheetTitle = {
                            Text(
                                modifier = Modifier
                                    .imePadding()
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                text = "Виберіть код",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.background,
                        onItemSelected = {
                            codeText.value = it.dial_code
                            openBottomSheet = false
                        }, onDismissRequest = {
                            openBottomSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun EnterPhonePreview() {
    EnterPhone(
        onCreateUser = {}
    )
}