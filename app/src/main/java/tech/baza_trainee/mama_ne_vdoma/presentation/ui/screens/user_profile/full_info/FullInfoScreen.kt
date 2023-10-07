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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.palm.composestateevents.EventEffect
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.TopBarWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun FullInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<FullInfoViewState> = mutableStateOf(FullInfoViewState()),
    onHandleEvent: (FullProfileEvent) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    onEditUser: () -> Unit = {},
    onAddChild: () -> Unit = {},
    onEditChild: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        BackHandler { onBack() }

        val context = LocalContext.current

        EventEffect(
            event = screenState.value.requestSuccess,
            onConsumed = { onHandleEvent(FullProfileEvent.ConsumeRequestSuccess) }
        ) { onNext() }

        EventEffect(
            event = screenState.value.userDeleted,
            onConsumed = { onHandleEvent(FullProfileEvent.ConsumeDeleteRequest) }
        ) { onDelete() }

        EventEffect(
            event = screenState.value.requestError,
            onConsumed = { onHandleEvent(FullProfileEvent.ConsumeRequestError) }
        ) { if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

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

        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            TopBarWithOptArrow(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = "Дані Вашого профілю",
                onBack = onBack
            )

            val scrollState = rememberScrollState()

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .constrainAs(content) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = modifier.height(16.dp))

                Text(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = "Ви:",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = modifier.height(8.dp))

                if (screenState.value.isUserInfoFilled)
                    ParentInfoDesk(
                        name = screenState.value.name.ifEmpty { "Введіть Ваше ім'я" },
                        address = screenState.value.address.ifEmpty { "Вкажіть Вашу адресу" },
                        avatar = screenState.value.userAvatar.asImageBitmap(),
                        schedule = screenState.value.schedule,
                        onEdit = onEditUser,
                        onDelete = { onHandleEvent(FullProfileEvent.DeleteUser) }
                    )
                else
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(colorAnimatable.value)
                            .clickable { onEditUser() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Надайте Ваші дані",
                            fontFamily = redHatDisplayFontFamily,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                Spacer(modifier = modifier.height(32.dp))

                Text(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = "Діти:",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                screenState.value.children.forEach { child ->
                    Spacer(modifier = modifier.height(8.dp))

                    ChildInfoDesk(
                        child,
                        onEdit = {
                            onHandleEvent(FullProfileEvent.SetChild(it))
                            onEditChild()
                        },
                        onDelete = {
                            onHandleEvent(FullProfileEvent.DeleteChild(it))
                        }
                    )
                }

                Spacer(modifier = modifier.height(16.dp))

                val animateAddChildBackground = !screenState.value.isChildInfoFilled &&
                        screenState.value.isUserInfoFilled

                Row(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(
                            color = if (!animateAddChildBackground) MaterialTheme.colorScheme.background
                            else colorAnimatable.value
                        )
                        .clickable(enabled = screenState.value.isUserInfoFilled) {
                            onHandleEvent(FullProfileEvent.ResetChild)
                            onAddChild()
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
                        modifier = modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth(1f),
                        color = if (screenState.value.isUserInfoFilled) MaterialTheme.colorScheme.onBackground
                        else SlateGray
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
                onClick = onNext
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
