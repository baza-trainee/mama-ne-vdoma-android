package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CountryCodePicker
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.UserAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun UserInfoScreen(
    screenState: UserInfoViewState,
    uiState: State<UserInfoUiState>,
    handleEvent: (UserInfoEvent) -> Unit
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(UserInfoEvent.OnBack) }

        val context = LocalContext.current

        when (val state = uiState.value) {
            UserInfoUiState.Idle -> Unit
            is UserInfoUiState.OnError -> {
                context.showToast(state.error)
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

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = "Заповнення профілю",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .height(192.dp)
                    .width(192.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenState.userAvatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                UserAvatarWithCameraAndGallery(
                    modifier = Modifier.fillMaxWidth(),
                    setUriForCrop = {
                        handleEvent(UserInfoEvent.SetImageToCrop(it))
                    },
                    onEditPhoto = { handleEvent(UserInfoEvent.OnEditPhoto) },
                    onDeletePhoto = { handleEvent(UserInfoEvent.OnDeletePhoto) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                value = screenState.name,
                label = "Введіть своє ім'я (нікнейм)",
                hint = "Ім'я (нікнейм)",
                onValueChange = { handleEvent(UserInfoEvent.ValidateUserName(it)) },
                isError = screenState.nameValid == ValidField.INVALID,
                errorText = "Ви ввели некоректнe ім'я",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Ваше ім’я повинне складатись із 2-18 символів і може містити букви та цифри, а також пробіли та дефіси",
                textAlign = TextAlign.Start,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            CountryCodePicker(
                currentCode = screenState.code,
                currentPhone = screenState.phone,
                isPhoneValid = screenState.phoneValid,
                countries = screenState.countries,
                onCodeSelected = {
                    handleEvent(UserInfoEvent.SetCode(it.phoneCode, it.countryCode))
                },
                onPhoneChanged = {
                    handleEvent(UserInfoEvent.ValidatePhone(it))
                },
                onKeyBoardAction = { handleEvent(UserInfoEvent.SaveInfo) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(UserInfoEvent.SaveInfo) },
                enabled = screenState.nameValid == ValidField.VALID &&
                        screenState.phoneValid == ValidField.VALID &&
                        screenState.code.isNotEmpty()
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun UserInfoPreview() {
    UserInfoScreen(
        screenState = UserInfoViewState(),
        uiState = remember {
            mutableStateOf(UserInfoUiState.Idle)
        },
        handleEvent = { _ -> }
    )
}