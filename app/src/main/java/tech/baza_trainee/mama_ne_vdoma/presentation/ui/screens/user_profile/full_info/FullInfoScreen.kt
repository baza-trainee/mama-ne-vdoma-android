package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun FullInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<FullInfoViewState> = mutableStateOf(FullInfoViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (FullInfoEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        BackHandler { handleEvent(FullInfoEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(FullInfoEvent.ResetUiState)
            }
        }

        val colorAnimatable = remember { Animatable(Color.White) }

        val infiniteTransition = rememberInfiniteTransition(label = "")

        val color by infiniteTransition.animateColor(
            initialValue = Color.Yellow,
            targetValue = Color.White,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        LaunchedEffect(key1 = color) {
            colorAnimatable.animateTo(color)
        }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = "Дані Вашого профілю",
                onBack = { handleEvent(FullInfoEvent.OnBack) }
            )

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Ви:",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (screenState.value.isUserInfoFilled)
                    ParentInfoDesk(
                        modifier = Modifier.fillMaxWidth(),
                        name = screenState.value.name.ifEmpty { "Введіть Ваше ім'я" },
                        address = screenState.value.address.ifEmpty { "Вкажіть Вашу адресу" },
                        avatar = screenState.value.userAvatar,
                        schedule = screenState.value.schedule,
                        onEdit = { handleEvent(FullInfoEvent.EditUser) },
                        onDelete = { handleEvent(FullInfoEvent.DeleteUser) }
                    )
                else
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(colorAnimatable.value)
                            .clickable { handleEvent(FullInfoEvent.EditUser) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Надайте Ваші дані",
                            fontFamily = redHatDisplayFontFamily,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Діти:",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                screenState.value.children.forEach { child ->
                    Spacer(modifier = Modifier.height(8.dp))

                    ChildInfoDesk(
                        modifier = Modifier.fillMaxWidth(),
                        child = child,
                        onEdit = {
                            handleEvent(FullInfoEvent.EditChild(it))
                        },
                        onDelete = {
                            handleEvent(FullInfoEvent.DeleteChild(it))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val animateAddChildBackground = !screenState.value.isChildInfoFilled &&
                        screenState.value.isUserInfoFilled

                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(
                            color = if (!animateAddChildBackground) MaterialTheme.colorScheme.background
                            else colorAnimatable.value
                        )
                        .clickable(enabled = screenState.value.isUserInfoFilled) {
                            handleEvent(FullInfoEvent.AddChild)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = if (screenState.value.isChildInfoFilled) MaterialTheme.colorScheme.primary
                        else SlateGray
                    )
                    Text(
                        text = if (screenState.value.isChildInfoFilled) "Додати ще дитину"
                        else "Додати дитину",
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth(1f),
                        color = if (screenState.value.isUserInfoFilled) MaterialTheme.colorScheme.onBackground
                        else SlateGray
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { handleEvent(FullInfoEvent.OnNext) },
                enabled = screenState.value.isUserInfoFilled && screenState.value.isChildInfoFilled
            ) {
                ButtonText(
                    text = "Підтвердити"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun FullInfoPreview() {
    FullInfoScreen()
}
