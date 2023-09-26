package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canopas.campose.countrypicker.CountryPickerBottomSheet
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserPhoneViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun EnterPhoneFunc(
    viewModel: UserSettingsViewModel,
    onCreateUser: () -> Unit
) {
    EnterPhone(
        screenState = viewModel.phoneScreenState.collectAsStateWithLifecycle(),
        setCode = { viewModel.setCode(it) },
        validatePhone = { viewModel.validatePhone(it) },
        onCreateUser = onCreateUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPhone(
    modifier: Modifier = Modifier,
    screenState: State<UserPhoneViewState> = mutableStateOf(UserPhoneViewState()),
    setCode: (String) -> Unit = {},
    validatePhone: (String) -> Unit = {},
    onCreateUser: () -> Unit
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            val (title, input, error, next) = createRefs()

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

            var isPhoneFocused by remember { mutableStateOf(false) }

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
                    value = screenState.value.code,
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
                        disabledBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                )

                val focusRequester = remember { FocusRequester() }

                OutlinedTextField(
                    modifier = modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isPhoneFocused = it.isFocused
                        }
                        .weight(.75f)
                        .padding(end = 24.dp),
                    value = screenState.value.userPhone,
                    label = { Text("Введіть свій номер телефону") },
                    onValueChange = { validatePhone(it) },
                    isError = screenState.value.phoneValid == ValidField.INVALID && isPhoneFocused,
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
                    shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
                    enabled = screenState.value.code.isNotEmpty()
                )
            }
            if (screenState.value.phoneValid == ValidField.INVALID && isPhoneFocused) {
                Text(
                    text = "Ви ввели некоректний номер",
                    color = Color.Red,
                    modifier = modifier
                        .constrainAs(error) {
                            top.linkTo(input.bottom, 4.dp)
                        }
                        .padding(horizontal = 24.dp)
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
                onClick = onCreateUser,
                enabled = screenState.value.phoneValid == ValidField.VALID &&
                        screenState.value.code.isNotEmpty()
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
                        setCode(it.dial_code)
                        openBottomSheet = false
                    }, onDismissRequest = {
                        openBottomSheet = false
                    }
                )
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