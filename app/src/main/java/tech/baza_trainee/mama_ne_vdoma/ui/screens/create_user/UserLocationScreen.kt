package tech.baza_trainee.mama_ne_vdoma.ui.screens.create_user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import tech.baza_trainee.mama_ne_vdoma.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.utils.LocationPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.utils.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.utils.findActivity
import tech.baza_trainee.mama_ne_vdoma.utils.openAppSettings

@Composable
fun UserLocationFunc(
    onNext: () -> Unit
) {
    UserLocation(
        onNext = onNext
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLocation(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {}
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize()
        ) {
            val activity = LocalContext.current.findActivity()
            val permissionDialogQueue = remember { mutableStateListOf<String>() }

            val permissions = arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
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
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        activity,
                        it
                    ),
                    onDismiss = { permissionDialogQueue.remove(it) },
                    onGranted = {
                        permissionDialogQueue.remove(it)
                        multiplePermissionResultLauncher.launch(permissions)
                    },
                    onGoToAppSettingsClick = { activity.openAppSettings() })
            }

            ConstraintLayout(
                modifier = modifier.fillMaxWidth()
            ) {
                val (title, map, edit, btnNext) = createRefs()

                val topGuideline = createGuidelineFromTop(0.2f)

                Column(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(topGuideline)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Ваше місцезнаходження",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                        text = "Будь ласка, оберіть ваше місцерозташування," +
                                " щоб ви могли підібрати найближчі групи до вас",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                val singapore = LatLng(1.35, 103.87)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 10f)
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
                        state = MarkerState(position = singapore),
                        title = "Singapore",
                        snippet = "Marker in Singapore"
                    )
                }

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = modifier
                        .constrainAs(edit) {
                            bottom.linkTo(btnNext.top, 16.dp)
                            height = Dimension.fillToConstraints
                        }
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    label = { Text("Введіть вашу адресу") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    ),
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
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
                    Text(text = "Далі")
                }
            }
        }
    }
}

@Composable
@Preview
fun UserLocationPreview() {
    UserLocation()
}