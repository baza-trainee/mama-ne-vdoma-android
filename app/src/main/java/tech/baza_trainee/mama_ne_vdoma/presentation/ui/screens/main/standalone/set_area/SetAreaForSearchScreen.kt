package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.AddressNotCheckedDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.LocationUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SemiTransparent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SliderColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_20_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAreaForSearchScreen(
    screenState: SetAreaViewState = SetAreaViewState(),
    uiState: State<LocationUiState> = mutableStateOf(LocationUiState.Idle),
    handleEvent: (SetAreaEvent) -> Unit = { _ -> }
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.title_group_search),
                avatar = screenState.avatar,
                showNotification = true,
                notificationCount = screenState.notifications,
                onNotificationsClicked = { handleEvent(SetAreaEvent.GoToNotifications) },
                onAvatarClicked = { handleEvent(SetAreaEvent.OnAvatarClicked) },
                onBack = { handleEvent(SetAreaEvent.OnBack) }
            )
        }
    ) { paddingValues ->

        BackHandler { handleEvent(SetAreaEvent.OnBack) }

        val context = LocalContext.current

        var showAddressDialog by rememberSaveable { mutableStateOf(false) }
        var dialogTitle by rememberSaveable { mutableStateOf("") }

        when (val state = uiState.value) {
            LocationUiState.Idle -> Unit
            is LocationUiState.OnError -> {
                context.showToast(state.error)
                handleEvent(SetAreaEvent.ResetUiState)
            }

            LocationUiState.AddressNotChecked -> {
                showAddressDialog = true
                dialogTitle = stringResource(id = R.string.address_not_checked_info)
                handleEvent(SetAreaEvent.ResetUiState)
            }

            LocationUiState.AddressNotFound -> {
                showAddressDialog = true
                dialogTitle = stringResource(id = R.string.address_not_found)
                handleEvent(SetAreaEvent.ResetUiState)
            }
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        val KM = 1000

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomGoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                location = screenState.currentLocation,
                showMyLocationButton = isPermissionGranted,
                onMyLocationButtonClick = { handleEvent(SetAreaEvent.RequestUserLocation) },
                onMapClick = { handleEvent(SetAreaEvent.OnMapClick(it)) }
            ) {
                Marker(
                    state = MarkerState(position = screenState.currentLocation),
                    title = stringResource(id = R.string.you_are_here),
                    snippet = stringResource(id = R.string.current_location)
                )

                Circle(
                    center = screenState.currentLocation,
                    radius = screenState.radius.toDouble(),
                    strokeColor = MaterialTheme.colorScheme.primary,
                    fillColor = SemiTransparent
                )
            }

            val color = infiniteColorAnimation(
                initialValue = Color.White,
                targetValue = Color.Red,
                duration = 1000
            )

            OutlinedTextFieldWithError(
                value = screenState.address,
                onValueChange = {
                    handleEvent(
                        SetAreaEvent.UpdateUserAddress(it)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_8_dp)
                    .padding(horizontal = size_16_dp),
                label = stringResource(id = R.string.enter_your_address),
                hint = stringResource(id = R.string.address),
                trailingIcon = {
                    IconButton(
                        onClick = { handleEvent(SetAreaEvent.GetLocationFromAddress) },
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
                errorText = stringResource(id = R.string.address_not_checked)
            )

            Spacer(modifier = Modifier.height(size_8_dp))

            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = size_16_dp),
                value = screenState.radius / KM,
                onValueChange = {
                    handleEvent(SetAreaEvent.SetAreaRadius(it * KM))
                },
                colors = SliderDefaults.colors(
                    activeTrackColor = SliderColor
                ),
                valueRange = 1f..25f,
                thumb = { position ->
                    Column(
                        modifier = Modifier
                            .padding(bottom = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_slider_thumb),
                                contentDescription = "thumb"
                            )
                            Text(
                                modifier = Modifier
                                    .padding(bottom = size_8_dp),
                                text = position.value.toInt().toString(),
                                fontFamily = redHatDisplayFontFamily,
                                fontSize = font_size_12_sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(size_20_dp)
                                .height(size_20_dp)
                                .background(
                                    color = SliderColor,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            )

            Button(
                modifier = Modifier
                    .padding(all = size_16_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(SetAreaEvent.SaveArea) },
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_next)
                )
            }
        }

        if (showAddressDialog) {
            AddressNotCheckedDialog(
                title = dialogTitle
            ) { showAddressDialog = false }
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SetAreaForSearchScreenPreview() {
    SetAreaForSearchScreen(
        screenState = SetAreaViewState(),
        uiState = remember { mutableStateOf(LocationUiState.Idle) },
        handleEvent = { _ -> }
    )
}