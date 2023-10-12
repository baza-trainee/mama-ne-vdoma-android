package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.search_group.set_area

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun SetAreaForSearchScreen(
    modifier: Modifier = Modifier,
    screenState: State<SetAreaViewState> = mutableStateOf(SetAreaViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (SetAreaEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
        BackHandler { }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        ConstraintLayout(
            modifier = modifier.fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            HeaderWithToolbar(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = "Пошук групи",
                onBack = { }
            )

            if (isPermissionGranted) {
                ConstraintLayout(
                    modifier = modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .constrainAs(content) {
                            top.linkTo(topGuideline, 16.dp)
                            bottom.linkTo(btnNext.top, 16.dp)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    val (map, edit, picker) = createRefs()

                    val cameraPositionState = rememberCameraPositionState {
                        val cameraPosition = CameraPosition.Builder()
                            .target(screenState.value.currentLocation)
                            .zoom(15f)
                            .build()
                        position = cameraPosition
                    }

                    LaunchedEffect(screenState.value.currentLocation) {
                        val newCameraPosition =
                            CameraPosition.fromLatLngZoom(
                                screenState.value.currentLocation,
                                15f
                            )
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(newCameraPosition),
                            1_000
                        )
                    }

                    val uiSettings = remember {
                        MapUiSettings(myLocationButtonEnabled = true)
                    }
                    val properties by remember {
                        mutableStateOf(MapProperties(isMyLocationEnabled = true))
                    }

                    GoogleMap(
                        modifier = modifier
                            .fillMaxWidth()
                            .constrainAs(map) {
                                top.linkTo(parent.top)
                                bottom.linkTo(edit.top, 16.dp)
                                height = Dimension.fillToConstraints
                            },
                        cameraPositionState = cameraPositionState,
                        uiSettings = uiSettings,
                        properties = properties,
                        onMyLocationButtonClick = { true },
                        onMapClick = {}
                    ) {
                        Marker(
                            state = MarkerState(position = screenState.value.currentLocation),
                            title = "Ви тут",
                            snippet = "поточне місцезнаходження"
                        )
                    }

                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = modifier
                            .constrainAs(edit) {
                                bottom.linkTo(parent.bottom, 16.dp)
                            }
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
                                onClick = {  }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search_location"
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

//        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SetAreaForSearchScreenPreview() {
    SetAreaForSearchScreen()
}