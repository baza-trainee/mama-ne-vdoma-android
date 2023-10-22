package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.campose.countrypicker.CountryPickerBottomSheet
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.UserAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<UserInfoViewState> = mutableStateOf(UserInfoViewState()),
    uiState: State<UserInfoUiState> = mutableStateOf(UserInfoUiState.Idle),
    handleEvent: (UserInfoEvent) -> Unit = { _ -> }
) {
    SurfaceWithSystemBars(
        modifier = Modifier
    ) {
        BackHandler { handleEvent(UserInfoEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            UserInfoUiState.Idle -> Unit
            is UserInfoUiState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(UserInfoEvent.ResetUiState)
            }
            UserInfoUiState.OnAvatarError -> {
                Toast.makeText(
                    context,
                    "Аватарка має розмір більше 1МБ. Будь ласка, оберіть інше фото і повторіть",
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(UserInfoEvent.ResetUiState)
            }
        }

        var openBottomSheet by rememberSaveable { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .imePadding()
                .fillMaxWidth(),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp),
                    text = "Заповнення профілю",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserAvatarWithCameraAndGallery(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    avatar = screenState.value.userAvatar,
                    setUriForCrop = {
                        handleEvent(UserInfoEvent.SetImageToCrop(it))
                    },
                    onEditPhoto = { handleEvent(UserInfoEvent.OnEditPhoto) },
                    onDeletePhoto = { handleEvent(UserInfoEvent.OnDeletePhoto) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = screenState.value.name,
                    label = "Введіть своє ім'я (нікнейм)",
                    onValueChange = { handleEvent(UserInfoEvent.ValidateUserName(it)) },
                    isError = screenState.value.nameValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректнe ім'я"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Ваше ім’я повинне складатись із 2-18 символів і може містити букви та цифри, а також пробіли та дефіси",
                    textAlign = TextAlign.Start,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(16.dp))

                var isPhoneFocused by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
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
                            focusedBorderColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            disabledBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                    )

                    val focusRequester = remember { FocusRequester() }

                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isPhoneFocused = it.isFocused
                            }
                            .weight(.75f)
                            .padding(end = 24.dp),
                        value = screenState.value.phone,
                        label = { Text("Введіть свій номер телефону") },
                        onValueChange = { handleEvent(UserInfoEvent.ValidatePhone(it)) },
                        isError = screenState.value.phoneValid == ValidField.INVALID && isPhoneFocused,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                            disabledBorderColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
                        enabled = screenState.value.code.isNotEmpty(),
                        textStyle = TextStyle(
                            fontFamily = redHatDisplayFontFamily
                        )
                    )
                }
                if (screenState.value.phoneValid == ValidField.INVALID && isPhoneFocused) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ви ввели некоректний номер",
                        color = Color.Red,
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        fontFamily = redHatDisplayFontFamily,
                        style = TextStyle(
                            fontFamily = redHatDisplayFontFamily
                        ),
                        fontSize = 14.sp
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(UserInfoEvent.SaveInfo) },
                enabled = screenState.value.nameValid == ValidField.VALID &&
                        screenState.value.phoneValid == ValidField.VALID &&
                        screenState.value.code.isNotEmpty()
            ) {
                ButtonText(
                    text = "Далі"
                )
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    onItemSelected = {
                        handleEvent(UserInfoEvent.SetCode(it.dial_code, it.code))
                        openBottomSheet = false
                    }, onDismissRequest = {
                        openBottomSheet = false
                    }
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun UserInfoPreview() {
    UserInfoScreen()
}