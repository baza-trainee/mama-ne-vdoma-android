package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SemiTransparent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SliderColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAreaForSearchScreen(
    modifier: Modifier = Modifier,
    screenState: State<SetAreaViewState> = mutableStateOf(SetAreaViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (SetAreaEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
        BackHandler { handleEvent(SetAreaEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(SetAreaEvent.ResetUiState)
            }
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        val KM = 1000

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "Пошук групи",
                avatar = screenState.value.avatar,
                showNotification = false,
                onNotificationsClicked = {},
                onAvatarClicked = { handleEvent(SetAreaEvent.OnAvatarClicked) },
                onBack = { handleEvent(SetAreaEvent.OnBack) }
            )

            if (isPermissionGranted) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val (map, input, slider) = createRefs()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(map) {
                                top.linkTo(parent.top)
                                bottom.linkTo(input.top, 16.dp)
                                height = Dimension.fillToConstraints
                            }
                    ) {
                        CustomGoogleMap(
                            modifier = Modifier.fillMaxWidth(),
                            location = screenState.value.currentLocation,
                            onMyLocationButtonClick = { handleEvent(SetAreaEvent.RequestUserLocation) },
                            onMapClick = { handleEvent(SetAreaEvent.OnMapClick(it)) }
                        ) {
                            Marker(
                                state = MarkerState(position = screenState.value.currentLocation),
                                title = "Ви тут",
                                snippet = "поточне місцезнаходження"
                            )

                            Circle(
                                center = screenState.value.currentLocation,
                                radius = screenState.value.radius.toDouble(),
                                strokeColor = MaterialTheme.colorScheme.primary,
                                fillColor = SemiTransparent
                            )
                        }
                    }

                    OutlinedTextField(
                        value = screenState.value.address,
                        onValueChange = {
                            handleEvent(
                                SetAreaEvent.UpdateUserAddress(it)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .constrainAs(input) {
                                bottom.linkTo(slider.top, 8.dp)
                            },
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
                                onClick = { handleEvent(SetAreaEvent.GetLocationFromAddress) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search_location"
                                )
                            }
                        }
                    )

                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .constrainAs(slider) {
                                bottom.linkTo(parent.bottom, 8.dp)
                            },
                        value = screenState.value.radius / KM,
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
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(SetAreaEvent.SaveArea) }
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SetAreaForSearchScreenPreview() {
    SetAreaForSearchScreen()
}