package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.NotificationsPermissionTextProvider
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.PermissionDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings

@Composable
fun StartScreen(
    onStart: () -> Unit,
    onLogin: () -> Unit
) {
    SurfaceWithSystemBars {
        val activity = LocalContext.current.findActivity()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            var showRationale by rememberSaveable { mutableStateOf(false) }

            val notificationsPermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    if (!isGranted) {
                        if (activity.shouldShowRequestPermissionRationale(permission))
                            showRationale = true
                    }
                }
            )

            LaunchedEffect(key1 = true) {
                notificationsPermissionResultLauncher.launch(permission)
            }

            if (showRationale) {
                PermissionDialog(
                    permissionTextProvider = NotificationsPermissionTextProvider(LocalContext.current),
                    isPermanentlyDeclined = !ActivityCompat
                        .shouldShowRequestPermissionRationale(activity, permission),
                    onDismiss = { showRationale = false },
                    onGranted = { showRationale = false },
                    onGoToAppSettingsClick = { activity.openAppSettings() })
            }
        }

        BackHandler { activity.finish() }

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (image, btnStart, btnLogin) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = size_24_dp)
                        bottom.linkTo(btnStart.top, margin = size_24_dp)
                        height = Dimension.fillToConstraints
                    },
                painter = painterResource(id = R.drawable.collage),
                contentDescription = "start",
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Fit
            )

            Button(
                modifier = Modifier
                    .constrainAs(btnStart) {
                        bottom.linkTo(btnLogin.top)
                    }
                    .fillMaxWidth()
                    .padding(all = size_16_dp)
                    .height(size_48_dp),
                onClick = onStart
            ) {
                Text(
                    text = stringResource(id = R.string.action_start),
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            Text(
                text = getTextWithUnderline(
                    stringResource(id = R.string.account_existed),
                    stringResource(id = R.string.action_log_in)
                ),
                modifier = Modifier
                    .constrainAs(btnLogin) {
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onLogin()
                    }
                    .fillMaxWidth()
                    .padding(all = size_16_dp),
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }
    }
}

@Composable
@Preview
fun StartScreenPreview() {
    StartScreen(
        onStart = {},
        onLogin = {}
    )
}