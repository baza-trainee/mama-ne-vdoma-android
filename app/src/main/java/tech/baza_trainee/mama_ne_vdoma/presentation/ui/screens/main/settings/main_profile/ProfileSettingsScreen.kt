package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.PrivacyPolicyBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.DangerousActionAlertDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSettingsScreen(
    screenState: ProfileSettingsViewState,
    uiState: State<RequestState>,
    handleEvent: (ProfileSettingsEvent) -> Unit
) {
    BackHandler { handleEvent(ProfileSettingsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(ProfileSettingsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(screenState.avatar)
                    .placeholder(R.drawable.ic_user_no_photo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                fallback = painterResource(id = R.drawable.ic_user_no_photo),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clip(CircleShape)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = screenState.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = "${screenState.code}${screenState.phone}",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                text = screenState.email,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .basicMarquee(),
                text = screenState.address,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }

        screenState.children.forEach {
            Spacer(modifier = Modifier.height(8.dp))

            ChildCard(
                child = it,
                infoOnly = true
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = stringResource(id = R.string.receive_emails),
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )
            Switch(
                checked = screenState.sendEmails,
                onCheckedChange = { handleEvent(ProfileSettingsEvent.ToggleEmail) }
            )
        }

        if (!screenState.sendEmails) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp)
                        .weight(1f),
                    text = stringResource(id = R.string.service_email_hint),
                    fontSize = 12.sp,
                    fontFamily = redHatDisplayFontFamily,
                    color = Color.Red,
                    lineHeight = 18.sp
                )
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "error",
                    tint = Color.Red
                )
            }
        }

        var showDeleteAccountAlertDialog by rememberSaveable { mutableStateOf(!screenState.isPolicyChecked) }

        PrivacyPolicyBlock(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            isChecked = screenState.isPolicyChecked,
            onCheckedChanged = {
                showDeleteAccountAlertDialog = !it
                handleEvent(ProfileSettingsEvent.UpdatePolicyCheck(it))
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(48.dp),
            onClick = { handleEvent(ProfileSettingsEvent.EditProfile) }
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit_profile"
            )
            ButtonText(
                text = stringResource(id = R.string.title_edit_profile)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            onClick = { handleEvent(ProfileSettingsEvent.EditCredentials) }
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.Lock,
                contentDescription = "edit_credentials"
            )
            ButtonText(
                text = stringResource(id = R.string.edit_credentials)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = { handleEvent(ProfileSettingsEvent.LogOut) },
            colors = ButtonDefaults.buttonColors(
                containerColor = LogoutButtonColor,
                contentColor = LogoutButtonTextColor
            )
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "exit"
            )
            ButtonText(
                text = stringResource(id = R.string.action_log_out)
            )
        }

        if (showDeleteAccountAlertDialog) {
            DangerousActionAlertDialog(
                text = stringResource(id = R.string.delete_account_info),
                button = stringResource(id = R.string.action_yes_delete_account),
                onDelete = { handleEvent(ProfileSettingsEvent.DeleteUser) },
                onDismissRequest = {
                    handleEvent(ProfileSettingsEvent.UpdatePolicyCheck(true))
                    showDeleteAccountAlertDialog = false
                }
            )
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun ProfileSettingsScreenPreview() {
    ProfileSettingsScreen(
        screenState = ProfileSettingsViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}