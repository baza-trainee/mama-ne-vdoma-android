package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canopas.campose.countrypicker.CountryPickerBottomSheet
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.UserAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    screenState: State<EditProfileViewState> = mutableStateOf(EditProfileViewState()),
    uiState: State<EditProfileUiState> = mutableStateOf(EditProfileUiState.Idle),
    handleEvent: (EditProfileEvent) -> Unit = { _ -> }
) {
    BackHandler { handleEvent(EditProfileEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        EditProfileUiState.Idle -> Unit
        is EditProfileUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(EditProfileEvent.ResetUiState)
        }
        EditProfileUiState.OnAvatarError -> {
            Toast.makeText(
                context,
                "Аватарка має розмір більше 1МБ. Будь ласка, оберіть інше фото і повторіть",
                Toast.LENGTH_LONG
            ).show()
            handleEvent(EditProfileEvent.ResetUiState)
        }
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Персональна інформація",
            fontSize = 16.sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Фото",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(98.dp)
                    .width(98.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenState.value.userAvatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = null
                )
                UserAvatarWithCameraAndGallery(
                    modifier = Modifier.fillMaxSize(),
                    avatar = Uri.EMPTY,
                    setUriForCrop = {
                        handleEvent(EditProfileEvent.SetImageToCrop(it))
                    },
                    onEditPhoto = { handleEvent(EditProfileEvent.OnEditPhoto) },
                    onDeletePhoto = { handleEvent(EditProfileEvent.OnDeletePhoto) }
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = "Формат файлу - jpg, jpeg, gif, tif, tiff, tga, bmp, png",
                fontSize = 11.sp,
                fontFamily = redHatDisplayFontFamily
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Логін",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleEvent(EditProfileEvent.VerifyEmail) },
                text = "Перевірити емейл",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary
            )
        }

        OutlinedTextFieldWithError(
            modifier = modifier.fillMaxWidth(),
            text = screenState.value.email,
            label = "Введіть свій email",
            onValueChange = { handleEvent(EditProfileEvent.ValidateEmail(it)) },
            isError = screenState.value.emailValid == ValidField.INVALID,
            errorText = "Ви ввели некоректний email",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Пароль",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )

        PasswordTextFieldWithError(
            modifier = modifier.fillMaxWidth(),
            password = screenState.value.password,
            onValueChange = { handleEvent(EditProfileEvent.ValidatePassword(it)) },
            isError = screenState.value.passwordValid == ValidField.INVALID
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = modifier.fillMaxWidth(),
            text = "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Нікнейм/Ім’я користувача",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )

        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            text = screenState.value.name,
            label = "Вкажіть своє ім'я",
            onValueChange = { handleEvent(EditProfileEvent.ValidateUserName(it)) },
            isError = screenState.value.nameValid == ValidField.INVALID,
            errorText = "Ви ввели некоректнe ім'я"
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Ваше ім’я повинне складатись із 2-18 символів і може містити букви та цифри, а також пробіли та дефіси",
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily
        )

        Spacer(modifier = modifier.height(16.dp))

        var isPhoneFocused by remember { mutableStateOf(false) }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Номер телефону",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(.25f)
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
                    .weight(.75f),
                value = screenState.value.phone,
                label = {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = "Введіть свій номер телефону"
                    )
                        },
                onValueChange = { handleEvent(EditProfileEvent.ValidatePhone(it)) },
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
            Spacer(modifier = modifier.height(4.dp))

            Text(
                text = "Ви ввели некоректний номер",
                color = Color.Red,
                modifier = modifier
                    .padding(horizontal = 24.dp),
                fontFamily = redHatDisplayFontFamily,
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = 14.sp
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
                    handleEvent(EditProfileEvent.SetCode(it.dial_code, it.code))
                    openBottomSheet = false
                }, onDismissRequest = {
                    openBottomSheet = false
                }
            )
        }
    }
}

@Composable
@Preview
fun EditProfileScreenPreview() {
    EditProfileScreen()
}