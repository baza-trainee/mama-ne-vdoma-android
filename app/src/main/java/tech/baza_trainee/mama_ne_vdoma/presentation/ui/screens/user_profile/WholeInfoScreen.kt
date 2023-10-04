package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ParentInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.TopBarWithArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullProfileEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun FullInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<FullInfoViewState> = mutableStateOf(FullInfoViewState()),
    onHandleEvent: (FullProfileEvent) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            TopBarWithArrow(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = "Збереження даних профілю",
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

                ParentInfoDesk(
                    name = screenState.value.name,
                    address = screenState.value.address,
                    avatar = screenState.value.userAvatar.asImageBitmap(),
                    schedule = screenState.value.schedule,
                    onEdit = {},
                    onDelete = {}
                )

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

                Spacer(modifier = modifier.height(8.dp))

                screenState.value.children.forEach { child ->
                    ChildInfoDesk(
                        child,
                        onEdit = {
                            onHandleEvent(FullProfileEvent.UpdateFullProfile)
                            onEdit()
                        },
                        onDelete = {
                            onHandleEvent(FullProfileEvent.UpdateFullProfile)
                        }
                    )
                    Spacer(modifier = modifier.height(16.dp))
                }

                Row(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .clickable {
                            onHandleEvent(FullProfileEvent.UpdateFullProfile)
                            onEdit()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Додати ще дитину",
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = 20.sp,
                        modifier = modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth(1f)
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
    }
}

@Composable
@Preview
fun FullInfoPreview() {
    FullInfoScreen()
}
