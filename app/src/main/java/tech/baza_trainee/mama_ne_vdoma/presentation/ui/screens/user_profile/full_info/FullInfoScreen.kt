package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_64_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun FullInfoScreen(
    screenState: FullInfoViewState,
    uiState: State<RequestState>,
    handleEvent: (FullInfoEvent) -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.title_your_profile),
                onBack = { handleEvent(FullInfoEvent.OnBack) }
            )
        }
    ) { paddingValues ->

        BackHandler { handleEvent(FullInfoEvent.OnBack) }

        val context = LocalContext.current

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(state.error)
                handleEvent(FullInfoEvent.ResetUiState)
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize()
                .padding(horizontal = size_16_dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                text = stringResource(id = R.string.you),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_18_sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(size_8_dp))

            val color = infiniteColorAnimation(
                initialValue = Color.White,
                targetValue = Color.Red,
                duration = 1000
            )

            if (screenState.isUserInfoFilled)
                ParentInfoDesk(
                    modifier = Modifier.fillMaxWidth(),
                    name = screenState.name.ifEmpty { stringResource(id = R.string.enter_your_name) },
                    address = screenState.address.ifEmpty { stringResource(id = R.string.enter_your_address) },
                    avatar = screenState.userAvatar,
                    schedule = screenState.schedule,
                    onEdit = { handleEvent(FullInfoEvent.EditUser) },
                    onDelete = { handleEvent(FullInfoEvent.DeleteUser) }
                )
            else
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(size_64_dp)
                        .border(
                            color = color,
                            shape = RoundedCornerShape(size_2_dp),
                            width = size_1_dp
                        )
                        .clickable { handleEvent(FullInfoEvent.EditUser) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.fill_your_profile),
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = font_size_20_sp,
                        textAlign = TextAlign.Center
                    )
                }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_32_dp),
                text = stringResource(id = R.string.children),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_18_sp,
                fontWeight = FontWeight.Bold
            )

            screenState.children.forEach { child ->
                Spacer(modifier = Modifier.height(size_8_dp))

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

            val animateAddChildBackground = !screenState.isChildInfoFilled &&
                    screenState.isUserInfoFilled

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp)
                    .height(size_32_dp)
                    .background(
                        color = if (!animateAddChildBackground) MaterialTheme.colorScheme.background
                        else color
                    )
                    .clickable(enabled = screenState.isUserInfoFilled) {
                        handleEvent(FullInfoEvent.AddChild)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = if (screenState.isChildInfoFilled) MaterialTheme.colorScheme.primary
                    else SlateGray
                )
                Text(
                    text = if (screenState.isChildInfoFilled)
                        stringResource(id = R.string.add_more_children)
                    else
                        stringResource(id = R.string.add_child),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = font_size_20_sp,
                    modifier = Modifier
                        .padding(start = size_8_dp)
                        .fillMaxWidth(1f),
                    color = if (screenState.isUserInfoFilled) MaterialTheme.colorScheme.onBackground
                    else SlateGray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(vertical = size_16_dp)
                    .fillMaxWidth()
                    .height(size_48_dp),
                onClick = { handleEvent(FullInfoEvent.OnNext) },
                enabled = screenState.isUserInfoFilled && screenState.isChildInfoFilled
            ) {
                ButtonText(
                    text = stringResource(id = R.string.actiom_confirm)
                )
            }

            if (screenState.isLoading) LoadingIndicator()
        }
    }
}

@Composable
@Preview
fun FullInfoPreview() {
    FullInfoScreen(
        screenState = FullInfoViewState(),
        uiState = remember {
            mutableStateOf(RequestState.Idle)
        },
        handleEvent = {}
    )
}
