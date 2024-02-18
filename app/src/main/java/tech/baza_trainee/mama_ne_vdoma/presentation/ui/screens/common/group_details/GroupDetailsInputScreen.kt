package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsInputScreen(
    screenState: GroupDetailsViewState,
    uiState: State<UpdateDetailsUiState>,
    isForEditing: Boolean = false,
    handleEvent: (GroupDetailsEvent) -> Unit
) {
    val context = LocalContext.current

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showAddressDialog by rememberSaveable { mutableStateOf(false) }
    var dialogTitle by rememberSaveable { mutableStateOf("") }

    var showKickDialog by rememberSaveable { mutableStateOf(false) }
    var kickDialogData by rememberSaveable { mutableStateOf(Pair("", emptyList<String>())) }

    when (val state = uiState.value) {
        UpdateDetailsUiState.Idle -> Unit
        is UpdateDetailsUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            )
                .show()
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnAvatarError -> {
            Toast.makeText(
                context,
                "Аватарка має розмір більше 1МБ. Будь ласка, оберіть інше фото і повторіть",
                Toast.LENGTH_LONG
            ).show()
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.OnSaved -> showSuccessDialog = true
        UpdateDetailsUiState.AddressNotChecked -> {
            showAddressDialog = true
            dialogTitle = "Ви не перевірили вказану адресу"
            handleEvent(GroupDetailsEvent.ResetUiState)
        }

        UpdateDetailsUiState.AddressNotFound -> {
            showAddressDialog = true
            dialogTitle = "Вказано неіснуючу адресу"
            handleEvent(GroupDetailsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
            label = "Введіть Вашу адресу",
            hint = "Адреса",
            trailingIcon = {
                IconButton(
                    onClick = { handleEvent(GroupDetailsEvent.GetLocationFromAddress) },
                    modifier = Modifier
                        .padding(4.dp)
                        .border(
                            width = 1.dp,
                            color = if (screenState.isAddressChecked) Color.Transparent else color,
                            shape = RoundedCornerShape(2.dp)
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
            errorText = "Адреса не перевірена",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { handleEvent(GroupDetailsEvent.GetLocationFromAddress) }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldWithError(
            value = screenState.name,
            onValueChange = { handleEvent(GroupDetailsEvent.UpdateName(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = "Введіть назву групи",
            hint = "Назва групи",
            isError = screenState.nameValid == ValidField.INVALID,
            errorText = "Ви ввели некоректну назву"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = "Назва групи повинна складатись від 6 до 18 символів, може містити латинські чи кириличні букви та цифри, пробіли, дефіси. НЕ є унікальною",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )

        if (isForEditing) {

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(4.dp)
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
                            .padding(end = 8.dp)
                            .height(40.dp)
                            .width(40.dp)
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
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                        Text(
                            text = "Адміністратор групи",
                            fontSize = 14.sp,
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

                Spacer(modifier = Modifier.height(4.dp))

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
                                        .padding(end = 8.dp)
                                        .height(24.dp)
                                        .width(24.dp)
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
                                    fontSize = 14.sp,
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Вік дитини",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp
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
                label = { Text("Від") },
                placeholder = { Text("Вік") },
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

            Spacer(modifier = Modifier.width(32.dp))

            OutlinedTextField(
                value = screenState.maxAge,
                onValueChange = { handleEvent(GroupDetailsEvent.UpdateMaxAge(it)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isMaxAgeFocused = it.isFocused
                    },
                label = { Text("До") },
                placeholder = { Text("Вік") },
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

        val minAgeErrorText = "Не може бути менше 1 та більше за макс. вік"
        val maxAgeErrorText = "Не може бути більше 18 та менше за мін. вік"
        val errorText = when {
            screenState.maxAgeValid == ValidField.INVALID && isMaxAgeFocused -> maxAgeErrorText
            screenState.minAgeValid == ValidField.INVALID && isMinAgeFocused -> minAgeErrorText
            else -> ""
        }
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Визначіть графік групи",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(4.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Фото групи",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp)
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
            label = "Опис групи",
            hint = "Введіть будь-які відомості, які Ви вважаєте важливими/корисними для інших користувачів",
            onValueChange = { handleEvent(GroupDetailsEvent.UpdateDescription(it)) },
            minLines = 2,
            maxLines = 2,
            isError = screenState.description.length > 1000
        )

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End),
            text = "до 1000 символів",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 12.sp,
            color = GrayText
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
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
                text = if (isForEditing) "Зберегти зміни" else "Створити нову групу"
            )
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            info = if (isForEditing) "Ваша група успішно змінена" else "Ваша група успішно створена",
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