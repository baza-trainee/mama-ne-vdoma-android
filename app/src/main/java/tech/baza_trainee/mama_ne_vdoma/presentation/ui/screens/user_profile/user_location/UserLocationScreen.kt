package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import de.palm.composestateevents.EventEffect
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LocationPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.TopBarWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings

@Composable
fun UserLocationScreen(
    modifier: Modifier = Modifier,
    screenState: State<UserLocationViewState> = mutableStateOf(UserLocationViewState()),
    onHandleLocationEvent: (UserLocationEvent) -> Unit = { _ -> },
    onNext: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        val context = LocalContext.current

        EventEffect(
            event = screenState.value.requestSuccess,
            onConsumed = {}
        ) { onNext() }

        EventEffect(
            event = screenState.value.requestError,
            onConsumed = { onHandleLocationEvent(UserLocationEvent.ConsumeRequestError) }
        ) { if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

        LaunchedEffect(key1 = true) {
            if (screenState.value.address.isEmpty())
                onHandleLocationEvent(UserLocationEvent.RequestUserLocation)
        }

        val activity = LocalContext.current.findActivity()
        val permissionDialogQueue = remember { mutableStateListOf<String>() }

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { perms ->
                if (perms.values.contains(false)) {
                    val permission = perms.filter { !it.value }.keys.first()
                    if (activity.shouldShowRequestPermissionRationale(permission))
                        permissionDialogQueue.add(permission)
                }
            }
        )

        LaunchedEffect(key1 = true) {
            multiplePermissionResultLauncher.launch(permissions)
        }

        permissionDialogQueue.reversed().forEach {
            PermissionDialog(
                permissionTextProvider = LocationPermissionTextProvider(),
                isPermanentlyDeclined = !ActivityCompat
                    .shouldShowRequestPermissionRationale(activity, it ),
                onDismiss = { permissionDialogQueue.remove(it) },
                onGranted = {
                    permissionDialogQueue.remove(it)
                    multiplePermissionResultLauncher.launch(permissions)
                },
                onGoToAppSettingsClick = { activity.openAppSettings() })
        }

        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth(),
        ) {
            val (title, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            TopBarWithOptArrow(
                modifier = modifier
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

            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(content) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    }
            ) {
                val cameraPositionState = rememberCameraPositionState {
                    val cameraPosition = CameraPosition.Builder()
                        .target(screenState.value.currentLocation)
                        .zoom(15f)
                        .build()
                    position = cameraPosition
                }

                LaunchedEffect(screenState.value.currentLocation) {
                    val newCameraPosition =
                        CameraPosition.fromLatLngZoom(screenState.value.currentLocation, 15f)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(newCameraPosition),
                        1_000
                    )
                }

                val (map, edit) = createRefs()

                GoogleMap(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(map) {
                            top.linkTo(parent.top)
                            bottom.linkTo(edit.top, 16.dp)
                            height = Dimension.fillToConstraints
                        },
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = screenState.value.currentLocation),
                        title = "Ви тут",
                        snippet = "поточне місцезнаходження"
                    )
                }

                OutlinedTextField(
                    value = screenState.value.address,
                    onValueChange = { onHandleLocationEvent(UserLocationEvent.UpdateUserAddress(it)) },
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
                            onClick = { onHandleLocationEvent(UserLocationEvent.GetLocationFromAddress) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search_location"
                            )
                        }
                    }
                )
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(48.dp),
                onClick = { onHandleLocationEvent(UserLocationEvent.SaveUserLocation) }
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