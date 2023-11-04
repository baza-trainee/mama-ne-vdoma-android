package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.GroupAvatarWithCameraAndGallery
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    modifier: Modifier = Modifier,
    screenState: State<CreateGroupViewState> = mutableStateOf(CreateGroupViewState()),
    uiState: State<CreateGroupUiState> = mutableStateOf(CreateGroupUiState.Idle),
    handleEvent: (CreateGroupEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { handleEvent(CreateGroupEvent.OnBack) }

        val context = LocalContext.current

        var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
        var showAddressDialog by rememberSaveable { mutableStateOf(false) }
        var dialogTitle by rememberSaveable { mutableStateOf("") }

        when (val state = uiState.value) {
            CreateGroupUiState.Idle -> Unit
            is CreateGroupUiState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                )
                    .show()
                handleEvent(CreateGroupEvent.ResetUiState)
            }

            CreateGroupUiState.OnAvatarError -> {
                Toast.makeText(
                    context,
                    "Аватарка має розмір більше 1МБ. Будь ласка, оберіть інше фото і повторіть",
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(CreateGroupEvent.ResetUiState)
            }

            CreateGroupUiState.OnGroupCreated -> showSuccessDialog = true
            CreateGroupUiState.AddressNotChecked -> {
                showAddressDialog = true
                dialogTitle = "Ви не перевірили вказану адресу"
                handleEvent(CreateGroupEvent.ResetUiState)
            }

            CreateGroupUiState.AddressNotFound -> {
                showAddressDialog = true
                dialogTitle = "Вказано неіснуючу адресу"
                handleEvent(CreateGroupEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Створення нової групи",
                avatar = screenState.value.userAvatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(CreateGroupEvent.OnAvatarClicked) },
                onBack = { handleEvent(CreateGroupEvent.OnBack) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextFieldWithError(
                    value = screenState.value.address,
                    onValueChange = {
                        handleEvent(CreateGroupEvent.UpdateGroupAddress(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Введіть Вашу адресу",
                    trailingIcon = {
                        IconButton(
                            onClick = { handleEvent(CreateGroupEvent.GetLocationFromAddress) }
                        ) {
                            if (screenState.value.isAddressChecked) {
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
                    isError = !screenState.value.isAddressChecked,
                    errorText = "Адреса не перевірена",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { handleEvent(CreateGroupEvent.GetLocationFromAddress) }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextFieldWithError(
                    value = screenState.value.name,
                    onValueChange = { handleEvent(CreateGroupEvent.UpdateName(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Назва групи",
                    isError = screenState.value.nameValid == ValidField.INVALID,
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
                        value = screenState.value.minAge,
                        onValueChange = { handleEvent(CreateGroupEvent.UpdateMinAge(it)) },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isMinAgeFocused = it.isFocused
                            },
                        label = { Text("Від") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        isError = screenState.value.minAgeValid == ValidField.INVALID && isMinAgeFocused,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        ),
                        trailingIcon = {
                            if (screenState.value.minAgeValid == ValidField.INVALID && isMinAgeFocused)
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
                        value = screenState.value.maxAge,
                        onValueChange = { handleEvent(CreateGroupEvent.UpdateMaxAge(it)) },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isMaxAgeFocused = it.isFocused
                            },
                        label = { Text("До") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        isError = screenState.value.maxAgeValid == ValidField.INVALID && isMaxAgeFocused,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        ),
                        trailingIcon = {
                            if (screenState.value.maxAgeValid == ValidField.INVALID && isMaxAgeFocused)
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
                    screenState.value.maxAgeValid == ValidField.INVALID && isMaxAgeFocused -> maxAgeErrorText
                    screenState.value.minAgeValid == ValidField.INVALID && isMinAgeFocused -> minAgeErrorText
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
                    schedule = screenState.value.schedule,
                    onValueChange = { day, period ->
                        handleEvent(
                            CreateGroupEvent.UpdateGroupSchedule(
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
                            .data(screenState.value.avatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )

                    GroupAvatarWithCameraAndGallery(
                        modifier = Modifier.fillMaxWidth(),
                        setUriForCrop = {
                            handleEvent(CreateGroupEvent.SetImageToCrop(it))
                        },
                        onEditPhoto = { handleEvent(CreateGroupEvent.OnEditPhoto) },
                        onDeletePhoto = { handleEvent(CreateGroupEvent.OnDeletePhoto) }
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = screenState.value.description,
                    label = { Text("Опис групи") },
                    onValueChange = { handleEvent(CreateGroupEvent.UpdateDescription(it)) },
                    minLines = 2,
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    ),
                    textStyle = TextStyle(
                        fontFamily = redHatDisplayFontFamily
                    )
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
                        handleEvent(CreateGroupEvent.OnCreate)
                    },
                    enabled = screenState.value.nameValid == ValidField.VALID &&
                            screenState.value.minAgeValid == ValidField.VALID &&
                            screenState.value.maxAgeValid == ValidField.VALID &&
                            screenState.value.description.isNotEmpty() &&
                            screenState.value.schedule.values.any { it.isFilled() }
                ) {
                    ButtonText(
                        text = "Створити нову групу"
                    )
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
                                text = "Ваша група успішно створена",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
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
                                        handleEvent(CreateGroupEvent.GoToMain)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }

                if (showAddressDialog) {
                    AlertDialog(onDismissRequest = { showAddressDialog = false }) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                text = dialogTitle,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
                                fontFamily = redHatDisplayFontFamily
                            )

                            Text(
                                text = "Зрозуміло",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .align(Alignment.End)
                                    .clickable {
                                        showAddressDialog = false
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CreateGroupScreenPreview() {
    CreateGroupScreen()
}