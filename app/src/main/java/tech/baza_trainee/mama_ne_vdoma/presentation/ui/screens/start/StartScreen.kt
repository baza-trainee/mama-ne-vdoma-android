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
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.openAppSettings

@Composable
fun Start(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    SurfaceWithSystemBars {

        val activity = LocalContext.current.findActivity()
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                notificationsPermissionResultLauncher.launch(permission)
        }

        if (showRationale) {
            PermissionDialog(
                permissionTextProvider = NotificationsPermissionTextProvider(),
                isPermanentlyDeclined = !ActivityCompat
                    .shouldShowRequestPermissionRationale(activity, permission),
                onDismiss = { showRationale = false },
                onGranted = { showRationale = false },
                onGoToAppSettingsClick = { activity.openAppSettings() })
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
                        top.linkTo(parent.top, margin = 24.dp)
                        bottom.linkTo(btnStart.top, margin = 24.dp)
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
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(48.dp),
                onClick = onStart
            ) {
                Text(
                    text = "Почати",
                    fontWeight = FontWeight.Bold,
                    fontFamily = redHatDisplayFontFamily
                )
            }

            Text(
                text = getTextWithUnderline("Вже є акаунт? ", "Увійти"),
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }
    }
}

@Composable
@Preview
fun StartScreenPreview() {
    Start()
}