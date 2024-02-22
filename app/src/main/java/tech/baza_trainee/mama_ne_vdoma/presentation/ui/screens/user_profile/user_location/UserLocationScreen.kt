package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.LocationUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun UserLocationScreen(
    screenState: UserLocationViewState,
    uiState: State<LocationUiState>,
    handleEvent: (UserLocationEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.your_current_location),
                info = stringResource(id = R.string.your_current_location_hint)
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current

        var showAddressDialog by rememberSaveable { mutableStateOf(false) }
        var dialogTitle by rememberSaveable { mutableStateOf("") }

        when (val state = uiState.value) {
            LocationUiState.Idle -> Unit

            is LocationUiState.OnError -> {
                context.showToast(state.error)
                handleEvent(UserLocationEvent.ResetUiState)
            }

            LocationUiState.AddressNotChecked -> {
                showAddressDialog = true
                dialogTitle = stringResource(id = R.string.address_not_checked_info)
                handleEvent(UserLocationEvent.ResetUiState)
            }

            LocationUiState.AddressNotFound -> {
                showAddressDialog = true
                dialogTitle = stringResource(id = R.string.address_not_found)
                handleEvent(UserLocationEvent.ResetUiState)
            }
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomGoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                location = screenState.currentLocation,
                showMyLocationButton = isPermissionGranted,
                onMyLocationButtonClick = { handleEvent(UserLocationEvent.RequestUserLocation) },
                onMapClick = { handleEvent(UserLocationEvent.OnMapClick(it)) }
            ) {
                Marker(
                    state = MarkerState(position = screenState.currentLocation),
                    title = stringResource(id = R.string.you_are_here),
                    snippet = stringResource(id = R.string.current_location)
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
                    handleEvent(UserLocationEvent.UpdateUserAddress(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
                label = stringResource(id = R.string.enter_your_address),
                hint = stringResource(id = R.string.address),
                trailingIcon = {
                    IconButton(
                        onClick = { handleEvent(UserLocationEvent.GetLocationFromAddress) },
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
                errorText = stringResource(id = R.string.address_not_checked),
                maxLines = 2,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { handleEvent(UserLocationEvent.GetLocationFromAddress) }
                )
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(UserLocationEvent.SaveUserLocation) }
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
fun UserLocationPreview() {
    UserLocationScreen(
        screenState = UserLocationViewState(),
        uiState = remember {
            mutableStateOf(LocationUiState.Idle)
        },
        handleEvent = { _ -> }
    )
}