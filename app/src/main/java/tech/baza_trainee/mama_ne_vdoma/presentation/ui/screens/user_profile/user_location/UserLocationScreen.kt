package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.CustomGoogleMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LocationPermission
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun UserLocationScreen(
    modifier: Modifier = Modifier,
    screenState: State<UserLocationViewState> = mutableStateOf(UserLocationViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (UserLocationEvent) -> Unit = { _ -> }
) {
    SurfaceWithNavigationBars {
        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(UserLocationEvent.ResetUiState)
            }
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        LocationPermission { isPermissionGranted = it }

        ConstraintLayout(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth(),
        ) {
            val (title, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            HeaderWithOptArrow(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                title = "Ваше місцезнаходження",
                info = "Будь ласка, оберіть ваше місцерозташування," +
                        " щоб ви могли підібрати найближчі групи до вас"
            )

            if (isPermissionGranted) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(content) {
                            top.linkTo(topGuideline)
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
                            onMyLocationButtonClick = { handleEvent(UserLocationEvent.RequestUserLocation) },
                            onMapClick = { handleEvent(UserLocationEvent.OnMapClick(it)) }
                        ) {
                            Marker(
                                state = MarkerState(position = screenState.value.currentLocation),
                                title = "Ви тут",
                                snippet = "поточне місцезнаходження"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = screenState.value.address,
                        onValueChange = {
                            handleEvent(
                                UserLocationEvent.UpdateUserAddress(it)
                            )
                        },
                        modifier = Modifier
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
                                onClick = { handleEvent(UserLocationEvent.GetLocationFromAddress) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search_location"
                                )
                            }
                        },
                        maxLines = 2
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(48.dp),
                onClick = { handleEvent(UserLocationEvent.SaveUserLocation) }
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
fun UserLocationPreview() {
    UserLocationScreen()
}