package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CountryCodePicker
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.UserAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.AddressNotCheckedDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.DangerousActionAlertDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.ChildScheduleEditDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs.ParentScheduleEditDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_11_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_160_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_96_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    screenState: EditProfileViewState,
    uiState: State<UpdateDetailsUiState>,
    handleEvent: (EditProfileEvent) -> Unit
) {
    var exitScreen by remember { mutableIntStateOf(-1) }

    BackHandler { exitScreen = 0 }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showAddressDialog by rememberSaveable { mutableStateOf(false) }
    var dialogTitle by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    when (val state = uiState.value) {
        UpdateDetailsUiState.Idle -> Unit
        is UpdateDetailsUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(EditProfileEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnAvatarError -> {
            Toast.makeText(
                context,
                stringResource(id = R.string.photo_size_error),
                Toast.LENGTH_LONG
            ).show()
            handleEvent(EditProfileEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnSaved -> showSuccessDialog = true
        UpdateDetailsUiState.AddressNotChecked -> {
            showAddressDialog = true
            dialogTitle = stringResource(id = R.string.address_not_checked_info)
            handleEvent(EditProfileEvent.ResetUiState)
        }

        UpdateDetailsUiState.AddressNotFound -> {
            showAddressDialog = true
            dialogTitle = stringResource(id = R.string.address_not_found)
            handleEvent(EditProfileEvent.ResetUiState)
        }
    }

    var editUserSchedule by rememberSaveable { mutableStateOf(false) }
    var editChildSchedule by rememberSaveable { mutableStateOf(false) }
    var deleteChildDialog by rememberSaveable { mutableStateOf(false) }
    var selectedChild by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.personal_info),
            fontSize = font_size_16_sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )
        //Photo
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            text = stringResource(id = R.string.photo),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(size_96_dp)
                    .width(size_96_dp),
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
                        handleEvent(EditProfileEvent.SetImageToCrop(it))
                    },
                    onEditPhoto = { handleEvent(EditProfileEvent.OnEditPhoto) },
                    onDeletePhoto = { handleEvent(EditProfileEvent.OnDeletePhoto) }
                )
            }

            Spacer(modifier = Modifier.width(size_4_dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = stringResource(id = R.string.file_format),
                fontSize = font_size_11_sp,
                fontFamily = redHatDisplayFontFamily,
                color = GrayText
            )
        }

        //Nickname
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            text = stringResource(id = R.string.nickname),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )

        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.name,
            label = stringResource(id = R.string.enter_your_name),
            hint = stringResource(id = R.string.name_nickname),
            onValueChange = { handleEvent(EditProfileEvent.ValidateUserName(it)) },
            isError = screenState.nameValid == ValidField.INVALID,
            isHighlighted = screenState.nameValid == ValidField.EMPTY,
            errorText = stringResource(id = R.string.incorrect_name)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = size_16_dp)
                .padding(top = size_4_dp),
            text = stringResource(id = R.string.name_rule_hint),
            fontSize = font_size_11_sp,
            fontFamily = redHatDisplayFontFamily,
            color = GrayText,
            lineHeight = font_size_18_sp
        )

        //Phone number
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            text = stringResource(id = R.string.phone_number),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )

        CountryCodePicker(
            currentCode = screenState.code,
            currentPhone = screenState.phone,
            isPhoneValid = screenState.phoneValid,
            countries = screenState.countries,
            onCodeSelected = {
                handleEvent(EditProfileEvent.SetCode(it.phoneCode, it.countryCode))
            },
            onPhoneChanged = {
                handleEvent(EditProfileEvent.ValidatePhone(it))
            }
        )

        //Location
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            text = stringResource(id = R.string.location),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        CustomGoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(size_160_dp),
            location = screenState.currentLocation,
            showMyLocationButton = isPermissionGranted,
            onMyLocationButtonClick = { handleEvent(EditProfileEvent.RequestUserLocation) },
            onMapClick = { handleEvent(EditProfileEvent.OnMapClick(it)) }
        ) {
            Marker(
                state = MarkerState(position = screenState.currentLocation),
                title = stringResource(id = R.string.you_are_here),
                snippet = stringResource(id = R.string.current_location)
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = size_16_dp)
                .padding(top = size_4_dp),
            text = stringResource(id = R.string.choose_or_enter_location),
            fontSize = font_size_11_sp,
            fontFamily = redHatDisplayFontFamily,
            color = GrayText,
            lineHeight = font_size_18_sp
        )

        val color = infiniteColorAnimation(
            initialValue = Color.White,
            targetValue = Color.Red,
            duration = 1000
        )

        OutlinedTextFieldWithError(
            value = screenState.address,
            onValueChange = {
                handleEvent(EditProfileEvent.UpdateUserAddress(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            label = stringResource(id = R.string.enter_your_address),
            hint = stringResource(id = R.string.address),
            trailingIcon = {
                IconButton(
                    onClick = { handleEvent(EditProfileEvent.GetLocationFromAddress) },
                    modifier = Modifier
                        .padding(size_4_dp)
                        .border(
                            width = size_1_dp,
                            color = if (screenState.isAddressChecked || screenState.address.isEmpty()) Color.Transparent else color,
                            shape = RoundedCornerShape(size_2_dp)
                        )
                ) {
                    if (screenState.isAddressChecked) {
                        Icon(
                            painterResource(id = R.drawable.ic_done),
                            contentDescription = "search_location",
                        )
                    } else if (screenState.address.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "search_location"
                        )
                    }
                }
            },
            isError = !screenState.isAddressChecked && screenState.address.isNotEmpty(),
            isHighlighted = screenState.address.isEmpty(),
            errorText = stringResource(id = R.string.address_not_checked),
            maxLines = 2
        )

        //Schedule Info
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            text = stringResource(id = R.string.my_schedule),
            fontSize = font_size_16_sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )

        ParentInfoDesk(
            modifier = Modifier.fillMaxWidth(),
            name = screenState.name,
            avatar = screenState.userAvatar,
            address = "",
            showDeleteButton = false,
            schedule = screenState.schedule,
            onEdit = { editUserSchedule = true }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.children_info),
                fontSize = font_size_16_sp,
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
                text = stringResource(id = R.string.add_child_plus),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_14_sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                color = if (screenState.children.isEmpty()) color else MaterialTheme.colorScheme.primary
            )
        }

        screenState.children.forEachIndexed { index, child ->
            if (index != 0)
                Spacer(modifier = Modifier.height(size_8_dp))

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
                .padding(vertical = size_16_dp)
                .height(size_48_dp),
            onClick = { handleEvent(EditProfileEvent.SaveInfo) }
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_save_changes)
            )
        }

        var showDeleteAccountAlertDialog by rememberSaveable { mutableStateOf(false) }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = size_16_dp)
                .height(size_48_dp),
            onClick = { showDeleteAccountAlertDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_delete_account)
            )
        }

        if (editUserSchedule) {
            ParentScheduleEditDialog(
                schedule = screenState.schedule,
                note = screenState.note,
                onSave = { schedule, note ->
                    handleEvent(
                        EditProfileEvent.SaveParentInfo(
                            schedule,
                            note
                        )
                    )
                },
                onDismissRequest = { editUserSchedule = false }
            )
        }

        if (editChildSchedule) {
            ChildScheduleEditDialog(
                selectedChild = selectedChild,
                children = screenState.children,
                onSave = { schedules, notes ->
                    handleEvent(
                        EditProfileEvent.SaveChildren(
                            schedules,
                            notes
                        )
                    )
                },
                onDismissRequest = { editChildSchedule = false }
            )
        }

        if (showDeleteAccountAlertDialog) {
            DangerousActionAlertDialog(
                text = stringResource(id = R.string.delete_account_info),
                button = stringResource(id = R.string.action_yes_delete_account),
                onDelete = { handleEvent(EditProfileEvent.DeleteUser) },
                onDismissRequest = { showDeleteAccountAlertDialog = false }
            )
        }

        if (deleteChildDialog) {
            DangerousActionAlertDialog(
                text = stringResource(id = R.string.delete_child_info),
                button = stringResource(id = R.string.action_delete_child),
                onDismissRequest = {
                    selectedChild = 0
                    deleteChildDialog = false
                },
                onDelete = {
                    deleteChildDialog = false
                    handleEvent(EditProfileEvent.DeleteChild(screenState.children[selectedChild].childId))
                }
            )
        }

        if (exitScreen != -1) {
            AlertDialog(onDismissRequest = { exitScreen = -1 }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size_8_dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = size_8_dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = size_16_dp)
                            .padding(top = size_16_dp),
                        text = stringResource(id = R.string.warning_not_saved),
                        fontSize = font_size_14_sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = size_16_dp)
                            .padding(bottom = size_16_dp, top = size_24_dp),
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
                            text = stringResource(id = R.string.action_not_save),
                            fontSize = font_size_16_sp,
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
                            text = stringResource(id = R.string.action_save),
                            fontSize = font_size_16_sp,
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
                        .clip(RoundedCornerShape(size_8_dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(size_16_dp)
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
                            .padding(top = size_16_dp)
                            .padding(horizontal = size_16_dp)
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.info_updated),
                        fontSize = font_size_14_sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = stringResource(id = R.string.action_go_to_main),
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
                            .padding(size_16_dp)
                    )
                }
            }
        }

        if (showAddressDialog) {
            AddressNotCheckedDialog(
                title = dialogTitle
            ) { showAddressDialog = false }
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun EditProfileScreenPreview() {
    EditProfileScreen(
        screenState = EditProfileViewState(),
        uiState = remember { mutableStateOf(UpdateDetailsUiState.Idle) },
        handleEvent = {}
    )
}