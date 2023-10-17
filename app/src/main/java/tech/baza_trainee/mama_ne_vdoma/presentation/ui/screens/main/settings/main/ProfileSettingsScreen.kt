package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun ProfileSettingsScreen(
    modifier: Modifier = Modifier,
    screenState: State<ProfileSettingsViewState> = mutableStateOf(ProfileSettingsViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (ProfileSettingsEvent) -> Unit = {}
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
                    .data(screenState.value.avatar)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.no_photo),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = screenState.value.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${screenState.value.code}${screenState.value.phone}",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = screenState.value.email,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = screenState.value.address,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily
            )
        }

        screenState.value.children.forEach {
            Spacer(modifier = Modifier.height(8.dp))

            ChildCard(
                child = it,
                infoOnly = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = "Отримувати повідомлення на електронну пошту",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )
            Switch(
                checked = screenState.value.sendEmails,
                onCheckedChange = { handleEvent(ProfileSettingsEvent.ToggleEmail) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp)
                    .weight(1f),
                text = "Сервісні повідомлення з питань відновлення пароля чи підтвердження електронної пошти, відправляються автоматично і не можуть бути відхилені Користувачем",
                fontSize = 11.sp,
                fontFamily = redHatDisplayFontFamily,
                color = Color.Red
            )
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "error",
                tint = Color.Red
            )
        }

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = { handleEvent(ProfileSettingsEvent.EditProfile) }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit"
            )
            Spacer(modifier = Modifier.width(4.dp))
            ButtonText(
                text = "Редагувати інформацію"
            )
        }

        Button(
            modifier = modifier
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
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "exit"
            )
            Spacer(modifier = Modifier.width(4.dp))
            ButtonText(
                text = "Вийти з акаунту"
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ProfileSettingsScreenPreview() {
    ProfileSettingsScreen()
}