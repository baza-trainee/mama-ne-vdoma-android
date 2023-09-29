package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import android.Manifest
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.LocationPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.TopBarWithoutArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings

@Composable
fun UserLocationFunc(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit
) {
    viewModel.requestCurrentLocation()

    UserLocation(
        viewState = viewModel.locationScreenState.collectAsStateWithLifecycle(),
        onSearchUserAddress = { viewModel.getLocationFromAddress(it) },
        onNext = onNext
    )
}

@Composable
fun UserLocation(
    modifier: Modifier = Modifier,
    viewState: State<UserLocationViewState> = mutableStateOf(UserLocationViewState()),
    onSearchUserAddress: (String) -> Unit = {},
    onNext: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
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
            val (title, map, edit, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            TopBarWithoutArrow(
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

            val cameraPositionState = rememberCameraPositionState {
                val cameraPosition = CameraPosition.Builder()
                    .target(viewState.value.currentLocation)
                    .zoom(15f)
                    .build()
                position = cameraPosition
            }

            LaunchedEffect(viewState.value.currentLocation) {
                val newCameraPosition =
                    CameraPosition.fromLatLngZoom(viewState.value.currentLocation, 15f)
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(newCameraPosition),
                    1_000
                )
            }

            GoogleMap(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(map) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(edit.top, 16.dp)
                        height = Dimension.fillToConstraints
                    },
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = viewState.value.currentLocation),
                    title = "Ви тут",
                    snippet = "поточне місцезнаходження"
                )
            }

            val emailText = remember {
                mutableStateOf(TextFieldValue(viewState.value.userAddress))
            }

            OutlinedTextField(
                value = emailText.value,
                onValueChange = { emailText.value = it },
                modifier = modifier
                    .constrainAs(edit) {
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
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
                        onClick = { onSearchUserAddress(emailText.value.text) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search_location"
                        )
                    }
                }
            )

            Button(
                modifier = modifier
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = onNext
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }
    }
}

@Composable
@Preview
fun UserLocationPreview() {
    UserLocation()
}