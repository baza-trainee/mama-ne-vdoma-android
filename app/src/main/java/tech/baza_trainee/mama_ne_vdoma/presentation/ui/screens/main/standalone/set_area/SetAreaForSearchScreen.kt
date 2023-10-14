package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
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
    val KM = 1000

    SurfaceWithNavigationBars {
        BackHandler { }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

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

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            HeaderWithToolbar(
                modifier = Modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = "Пошук групи",
                onBack = { }
            )

            if (isPermissionGranted) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .constrainAs(content) {
                            top.linkTo(topGuideline, 16.dp)
                            bottom.linkTo(btnNext.top, 16.dp)
                            height = Dimension.fillToConstraints
                        },
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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
                                radius = KM * screenState.value.radius.toDouble(),
                                strokeColor = MaterialTheme.colorScheme.primary,
                                fillColor = SemiTransparent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = screenState.value.address,
                        onValueChange = {
                            handleEvent(
                                SetAreaEvent.UpdateUserAddress(it)
                            )
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
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
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        value = screenState.value.radius,
                        onValueChange = {
                            handleEvent(SetAreaEvent.SetAreaRadius(it))
                        },
                        colors = SliderDefaults.colors(
                            activeTrackColor = SliderColor
                        ),
                        valueRange = 1f..100f,
                        thumb = {
                            Column(
                                modifier = modifier
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
                                        modifier = modifier
                                            .padding(bottom = 8.dp),
                                        text = it.value.toInt().toString(),
                                        fontFamily = redHatDisplayFontFamily,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Box(
                                    modifier = modifier
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
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(48.dp),
                onClick = { },
                enabled = true
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