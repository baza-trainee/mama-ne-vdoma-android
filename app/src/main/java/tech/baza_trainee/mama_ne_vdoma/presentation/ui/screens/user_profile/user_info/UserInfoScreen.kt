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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
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
                    context.getString(R.string.photo_size_error),
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
                text = stringResource(id = R.string.title_fill_profile),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = redHatDisplayFontFamily
            )

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
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

            OutlinedTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                value = screenState.name,
                label = stringResource(id = R.string.enter_your_name),
                hint = stringResource(id = R.string.nickname),
                onValueChange = { handleEvent(UserInfoEvent.ValidateUserName(it)) },
                isError = screenState.nameValid == ValidField.INVALID,
                errorText = stringResource(id = R.string.incorrect_name),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = stringResource(id = R.string.name_rule_hint),
                textAlign = TextAlign.Start,
                fontFamily = redHatDisplayFontFamily
            )

            CountryCodePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                    text = stringResource(id = R.string.action_next)
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