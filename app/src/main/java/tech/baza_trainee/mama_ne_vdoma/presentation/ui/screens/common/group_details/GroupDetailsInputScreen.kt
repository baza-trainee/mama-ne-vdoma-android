package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.GroupAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.AddressNotCheckedDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.SuccessDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.dialogs.KickUserDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_40_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_88_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun GroupDetailsInputScreen(
    screenState: GroupDetailsViewState,
    uiState: UpdateDetailsUiState,
    isForEditing: Boolean = false,
    handleEvent: (GroupDetailsEvent) -> Unit
) {
    val context = LocalContext.current

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showAddressDialog by rememberSaveable { mutableStateOf(false) }
    var dialogTitle by rememberSaveable { mutableStateOf("") }

    var showKickDialog by rememberSaveable { mutableStateOf(false) }
    var kickDialogData by rememberSaveable { mutableStateOf(Pair("", emptyList<String>())) }

    when (uiState) {
        UpdateDetailsUiState.Idle -> Unit
        is UpdateDetailsUiState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnAvatarError -> {
            context.showToast(stringResource(id = R.string.photo_size_error))
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnSaved -> showSuccessDialog = true
        UpdateDetailsUiState.AddressNotChecked -> {
            showAddressDialog = true
            dialogTitle = stringResource(id = R.string.address_not_checked_info)
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.AddressNotFound -> {
            showAddressDialog = true
            dialogTitle = stringResource(id = R.string.address_not_found)
            handleEvent(GroupDetailsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
            .fillMaxSize()
            .padding(horizontal = size_16_dp)
    ) {
        val color = infiniteColorAnimation(
            initialValue = Color.White,
            targetValue = Color.Red,
            duration = 1000
        )
        OutlinedTextFieldWithError(
            value = screenState.address,
            onValueChange = {
                handleEvent(GroupDetailsEvent.UpdateGroupAddress(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.enter_your_address),
            hint = stringResource(id = R.string.address),
            trailingIcon = {
                IconButton(
                    onClick = { handleEvent(GroupDetailsEvent.GetLocationFromAddress) },
                    modifier = Modifier
                        .padding(size_4_dp)
                        .border(
                            width = size_1_dp,
                            color = if (screenState.isAddressChecked) Color.Transparent else color,
                            shape = RoundedCornerShape(size_2_dp)
                        )
                ) {
                    if (screenState.isAddressChecked) {
                        Icon(
                            painterResource(id = R.drawable.ic_done),
                            contentDescription = "search_location",
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "search_location"
                        )
                    }
                }
            },
            isError = !screenState.isAddressChecked,
            errorText = stringResource(id = R.string.address_not_checked),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { handleEvent(GroupDetailsEvent.GetLocationFromAddress) }
            )
        )

        OutlinedTextFieldWithError(
            value = screenState.name,
            onValueChange = { handleEvent(GroupDetailsEvent.UpdateName(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            label = stringResource(id = R.string.enter_group_name),
            hint = stringResource(id = R.string.group_name),
            isError = screenState.nameValid == ValidField.INVALID,
            errorText = stringResource(id = R.string.group_name_incorrect)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = size_8_dp),
            text = stringResource(id = R.string.group_name_hint),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_12_sp,
            lineHeight = font_size_14_sp
        )

        if (isForEditing) {
            var expanded by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_8_dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size_4_dp)
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    val admin = remember {
                        screenState.members.find { it.id == screenState.adminId } ?: MemberUiModel()
                    }

                    AsyncImage(
                        modifier = Modifier
                            .padding(end = size_8_dp)
                            .height(size_40_dp)
                            .width(size_40_dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(admin.avatar)
                            .placeholder(R.drawable.ic_user_no_photo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                        fallback = painterResource(id = R.drawable.ic_user_no_photo),
                        contentDescription = "member",
                        contentScale = ContentScale.Fit
                    )

                    Column {
                        Text(
                            text = admin.name,
                            fontSize = font_size_16_sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                        Text(
                            text = stringResource(id = R.string.group_admin),
                            fontSize = font_size_14_sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (expanded)
                                    R.drawable.outline_arrow_drop_up_24
                                else
                                    R.drawable.outline_arrow_drop_down_24
                            ),
                            contentDescription = "toggle_more",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(size_4_dp))

                if (expanded) {
                    screenState.members.sortedBy { it.name }.forEach {
                        if (it.id != screenState.adminId) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                IconButton(
                                    onClick = {
                                        kickDialogData = it.name to it.children
                                        showKickDialog = true
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_kick_member),
                                        contentDescription = "kick_user",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                AsyncImage(
                                    modifier = Modifier
                                        .padding(end = size_8_dp)
                                        .height(size_24_dp)
                                        .width(size_24_dp)
                                        .clip(CircleShape),
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(it.avatar)
                                        .placeholder(R.drawable.ic_user_no_photo)
                                        .crossfade(true)
                                        .build(),
                                    placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                                    fallback = painterResource(id = R.drawable.ic_user_no_photo),
                                    contentDescription = "member",
                                    contentScale = ContentScale.Fit
                                )

                                Text(
                                    text = it.name,
                                    fontSize = font_size_14_sp,
                                    fontFamily = redHatDisplayFontFamily
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                val scope = rememberCoroutineScope()

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val intent =
                                                Intent(
                                                    Intent.ACTION_SENDTO,
                                                    Uri.parse("mailto:${it.email}")
                                                )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            ContextCompat.startActivity(context, intent, null)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_mail),
                                        contentDescription = "email",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val intent =
                                                Intent(
                                                    Intent.ACTION_DIAL,
                                                    Uri.parse("tel:${it.phone}")
                                                )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            ContextCompat.startActivity(context, intent, null)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_phone),
                                        contentDescription = "phone",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            text = stringResource(id = R.string.child_age),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_14_sp
        )

        var isMinAgeFocused by rememberSaveable { mutableStateOf(false) }
        var isMaxAgeFocused by rememberSaveable { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = screenState.minAge,
                onValueChange = { handleEvent(GroupDetailsEvent.UpdateMinAge(it)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isMinAgeFocused = it.isFocused
                    },
                label = { Text(stringResource(id = R.string.from)) },
                placeholder = { Text(stringResource(id = R.string.age)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                isError = screenState.minAgeValid == ValidField.INVALID && isMinAgeFocused,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                ),
                trailingIcon = {
                    if (screenState.minAgeValid == ValidField.INVALID && isMinAgeFocused)
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = "error",
                            tint = Color.Red
                        )
                },
                textStyle = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(size_32_dp))

            OutlinedTextField(
                value = screenState.maxAge,
                onValueChange = { handleEvent(GroupDetailsEvent.UpdateMaxAge(it)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isMaxAgeFocused = it.isFocused
                    },
                label = { Text(stringResource(id = R.string.to)) },
                placeholder = { Text(stringResource(id = R.string.age)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                isError = screenState.maxAgeValid == ValidField.INVALID && isMaxAgeFocused,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                ),
                trailingIcon = {
                    if (screenState.maxAgeValid == ValidField.INVALID && isMaxAgeFocused)
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = "error",
                            tint = Color.Red
                        )
                },
                textStyle = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                maxLines = 1
            )
        }

        val minAgeErrorText = stringResource(id = R.string.min_age_error)
        val maxAgeErrorText = stringResource(id = R.string.max_age_error)
        val errorText = when {
            screenState.maxAgeValid == ValidField.INVALID && isMaxAgeFocused -> maxAgeErrorText
            screenState.minAgeValid == ValidField.INVALID && isMinAgeFocused -> minAgeErrorText
            else -> ""
        }
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(top = size_4_dp),
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = font_size_14_sp
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            text = stringResource(id = R.string.enter_group_schedule),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_14_sp
        )

        Spacer(modifier = Modifier.width(size_4_dp))

        ScheduleGroup(
            schedule = screenState.schedule,
            onValueChange = { day, period ->
                handleEvent(
                    GroupDetailsEvent.UpdateGroupSchedule(
                        day,
                        period
                    )
                )
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_8_dp),
            text = stringResource(id = R.string.group_photo),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_14_sp
        )

        Spacer(modifier = Modifier.width(size_4_dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size_88_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_4_dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(screenState.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            GroupAvatarWithCameraAndGallery(
                modifier = Modifier.fillMaxWidth(),
                setUriForCrop = {
                    handleEvent(GroupDetailsEvent.SetImageToCrop(it))
                },
                onEditPhoto = { handleEvent(GroupDetailsEvent.OnEditPhoto) },
                onDeletePhoto = { handleEvent(GroupDetailsEvent.OnDeletePhoto) }
            )
        }

        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.description,
            label = stringResource(id = R.string.group_description),
            hint = stringResource(id = R.string.group_description_hint),
            onValueChange = { handleEvent(GroupDetailsEvent.UpdateDescription(it)) },
            minLines = 2,
            maxLines = 2,
            isError = screenState.description.length > 1000
        )

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End),
            text = stringResource(id = R.string.note_text_length),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_12_sp,
            color = GrayText
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size_16_dp)
                .height(size_48_dp),
            onClick = {
                handleEvent(GroupDetailsEvent.OnSave)
            },
            enabled = screenState.nameValid == ValidField.VALID &&
                    screenState.minAgeValid == ValidField.VALID &&
                    screenState.maxAgeValid == ValidField.VALID &&
                    screenState.description.isNotEmpty() &&
                    screenState.schedule.values.any { it.isFilled() }
        ) {
            ButtonText(
                text = if (isForEditing)
                    stringResource(id = R.string.action_save_changes)
                else
                    stringResource(id = R.string.action_create_new_group)
            )
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            info = if (isForEditing)
                stringResource(id = R.string.group_updated)
            else
                stringResource(id = R.string.group_created),
            onDismiss = { showSuccessDialog = false },
            onClick = {
                showSuccessDialog = false
                handleEvent(GroupDetailsEvent.GoToMain)
            }
        )
    }

    if (showAddressDialog) {
        AddressNotCheckedDialog(
            title = dialogTitle
        ) { showAddressDialog = false }
    }

    if (showKickDialog) {
        KickUserDialog(
            userName = kickDialogData.first,
            onDismiss = { showKickDialog = false },
            onKick = {
                showKickDialog = false
                handleEvent(
                    GroupDetailsEvent.OnKick(kickDialogData.second)
                )
            }
        )
    }
}