package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.UserAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.DangerousActionAlertDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.ChildScheduleEditDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.ParentScheduleEditDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    screenState: State<EditProfileViewState> = mutableStateOf(EditProfileViewState()),
    uiState: State<EditProfileUiState> = mutableStateOf(EditProfileUiState.Idle),
    handleEvent: (EditProfileEvent) -> Unit = { _ -> }
) {
    var exitScreen by remember { mutableIntStateOf(-1) }

    BackHandler { exitScreen = 0 }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

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

        EditProfileUiState.OnProfileSaved -> showSuccessDialog = true
    }

    val focusRequester = remember { FocusRequester() }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var editUserSchedule by remember { mutableStateOf(false) }
    var editChildSchedule by remember { mutableStateOf(false) }
    var deleteChildDialog by remember { mutableStateOf(false) }
    var selectedChild by remember { mutableIntStateOf(0) }

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

        //Photo
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
                    modifier = Modifier.fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenState.value.userAvatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                UserAvatarWithCameraAndGallery(
                    modifier = Modifier.fillMaxWidth(),
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
                fontFamily = redHatDisplayFontFamily,
                color = GrayText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Nickname
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Ваше ім’я повинне складатись із 2-18 символів і може містити букви та цифри, а також пробіли та дефіси",
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily,
            color = GrayText,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Phone number
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

        Spacer(modifier = Modifier.height(16.dp))

        //Location
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Місце розташування",
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            CustomGoogleMap(
                modifier = Modifier.fillMaxWidth(),
                location = screenState.value.currentLocation,
                showMyLocationButton = isPermissionGranted,
                onMyLocationButtonClick = { handleEvent(EditProfileEvent.RequestUserLocation) },
                onMapClick = { handleEvent(EditProfileEvent.OnMapClick(it)) }
            ) {
                Marker(
                    state = MarkerState(position = screenState.value.currentLocation),
                    title = "Ви тут",
                    snippet = "поточне місцезнаходження"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Оберіть Ваше місцерозташування на карті або введіть назву у поле для пошуку",
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily,
            color = GrayText,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = screenState.value.address,
            onValueChange = {
                handleEvent(EditProfileEvent.UpdateUserAddress(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введіть вашу адресу") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            ),
            trailingIcon = {
                IconButton(
                    onClick = { handleEvent(EditProfileEvent.GetLocationFromAddress) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search_location"
                    )
                }
            },
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Schedule Info
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Мій розклад",
            fontSize = 16.sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )

        ParentInfoDesk(
            modifier = Modifier.fillMaxWidth(),
            name = screenState.value.name,
            avatar = screenState.value.userAvatar,
            address = "",
            showDeleteButton = false,
            schedule = screenState.value.schedule,
            onEdit = { editUserSchedule = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Інформація о дітях",
                fontSize = 16.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { exitScreen = 1 },
                text = "+ Додати дитину",
                fontFamily = redHatDisplayFontFamily,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary
            )
        }

        screenState.value.children.forEachIndexed { index, child ->
            if (index != 0)
                Spacer(modifier = Modifier.height(8.dp))

            ChildInfoDesk(
                modifier = Modifier.fillMaxWidth(),
                child = child,
                onEdit = {
                    selectedChild = index
                    editChildSchedule = true
                },
                onDelete = {
                    selectedChild = index
                    deleteChildDialog = true
                }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = { handleEvent(EditProfileEvent.SaveInfo) }
        ) {
            ButtonText(
                text = "Зберегти зміни"
            )
        }

        var showDeleteAccountAlertDialog by remember { mutableStateOf(false) }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(48.dp),
            onClick = { showDeleteAccountAlertDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            ButtonText(
                text = "Видалити акаунт"
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

        if (editUserSchedule) {
            ParentScheduleEditDialog(
                scheduleModel = screenState.value.schedule,
                note = screenState.value.note,
                onEditSchedule = { day, period ->
                    handleEvent(
                        EditProfileEvent.EditParentSchedule(
                            day,
                            period
                        )
                    )
                },
                onEditNote = { handleEvent(EditProfileEvent.EditParentNote(it)) },
                onSave = { handleEvent(EditProfileEvent.SaveParentInfo) },
                onRestore = { handleEvent(EditProfileEvent.RestoreParentInfo) }
            ) { editUserSchedule = false }
        }

        if (editChildSchedule) {
            ChildScheduleEditDialog(
                selectedChild = selectedChild,
                children = screenState.value.children,
                onEditSchedule = { child, day, period ->
                    handleEvent(
                        EditProfileEvent.EditChildSchedule(
                            child,
                            day,
                            period
                        )
                    )
                },
                onEditNote = { child: Int, note: String ->
                    handleEvent(
                        EditProfileEvent.EditChildNote(
                            child,
                            note
                        )
                    )
                },
                onSave = { handleEvent(EditProfileEvent.SaveChildren) },
                onRestore = { handleEvent(EditProfileEvent.RestoreChild(it)) }
            ) { editChildSchedule = false }
        }

        if (showDeleteAccountAlertDialog) {
            DangerousActionAlertDialog(
                text = "Після видалення акаунту немає можливості його відновити. Щоб користуватись після цього мобільним додатком, необхідно буде зареєструватись заново. Підтвердити видалення мого акаунту?",
                button = "Так, видалити акаунт",
                onDelete = { handleEvent(EditProfileEvent.DeleteUser) },
                onDismissRequest = { showDeleteAccountAlertDialog = false }
            )
        }

        if (deleteChildDialog) {
            DangerousActionAlertDialog(
                text = "Картка дитини та розклад по догляду за дитиною будуть видалені. Підтвердити видалення картки дитини?",
                button = "Так, видалити картку",
                onDismissRequest = {
                    selectedChild = 0
                    deleteChildDialog = false
                },
                onDelete = {
                    deleteChildDialog = false
                    handleEvent(EditProfileEvent.DeleteChild(screenState.value.children[selectedChild].childId))
                }
            )
        }

        if (exitScreen != -1) {
            AlertDialog(onDismissRequest = { exitScreen = -1 }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Ви залишаете цей екран. Всі незбережені зміни будуть втрачені",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    when (exitScreen) {
                                        0 -> handleEvent(EditProfileEvent.OnBack)
                                        1 -> handleEvent(EditProfileEvent.AddChild)
                                        else -> Unit
                                    }
                                    exitScreen = -1
                                },
                            text = "Не зберігати",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    when (exitScreen) {
                                        0 -> handleEvent(EditProfileEvent.OnSaveAndBack)
                                        1 -> handleEvent(EditProfileEvent.OnSaveAndAddChild)
                                        else -> Unit
                                    }
                                    exitScreen = -1
                                },
                            text = "Зберегти",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (showSuccessDialog) {
            AlertDialog(onDismissRequest = { showSuccessDialog = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ok),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = "Інформація успішно оновлена!",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = redHatDisplayFontFamily
                    )

                    Text(
                        text = "На головну",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.End)
                            .clickable {
                                showSuccessDialog = false
                                handleEvent(EditProfileEvent.GoToMain)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun EditProfileScreenPreview() {
    EditProfileScreen()
}