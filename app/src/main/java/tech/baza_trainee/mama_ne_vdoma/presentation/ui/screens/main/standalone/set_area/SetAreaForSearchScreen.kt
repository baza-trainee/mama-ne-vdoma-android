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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.AddressNotCheckedDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.LocationUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SemiTransparent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SliderColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAreaForSearchScreen(
    screenState: SetAreaViewState = SetAreaViewState(),
    uiState: State<LocationUiState> = mutableStateOf(LocationUiState.Idle),
    handleEvent: (SetAreaEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
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
                dialogTitle = "Ви не перевірили вказану адресу"
                handleEvent(SetAreaEvent.ResetUiState)
            }

            LocationUiState.AddressNotFound -> {
                showAddressDialog = true
                dialogTitle = "Вказано неіснуючу адресу"
                handleEvent(SetAreaEvent.ResetUiState)
            }
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        val KM = 1000

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Пошук групи",
                avatar = screenState.avatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(SetAreaEvent.OnAvatarClicked) },
                onBack = { handleEvent(SetAreaEvent.OnBack) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                CustomGoogleMap(
                    modifier = Modifier.fillMaxWidth(),
                    location = screenState.currentLocation,
                    showMyLocationButton = isPermissionGranted,
                    onMyLocationButtonClick = { handleEvent(SetAreaEvent.RequestUserLocation) },
                    onMapClick = { handleEvent(SetAreaEvent.OnMapClick(it)) }
                ) {
                    Marker(
                        state = MarkerState(position = screenState.currentLocation),
                        title = "Ви тут",
                        snippet = "поточне місцезнаходження"
                    )

                    Circle(
                        center = screenState.currentLocation,
                        radius = screenState.radius.toDouble(),
                        strokeColor = MaterialTheme.colorScheme.primary,
                        fillColor = SemiTransparent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                    .padding(horizontal = 16.dp),
                label = "Введіть Вашу адресу",
                hint = "Адреса",
                trailingIcon = {
                    IconButton(
                        onClick = { handleEvent(SetAreaEvent.GetLocationFromAddress) },
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
                errorText = "Адреса не перевірена"
            )

            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                                    .padding(bottom = 8.dp),
                                text = position.value.toInt().toString(),
                                fontFamily = redHatDisplayFontFamily,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
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
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(SetAreaEvent.SaveArea) },
            ) {
                ButtonText(
                    text = "Далі"
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