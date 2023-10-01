package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.TopBarWithArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun  ChildrenInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    SurfaceWithNavigationBars(
        modifier = modifier
    ) {
        val screenState = viewModel.childrenInfoScreenState.collectAsStateWithLifecycle()

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
                title = "Анкета дитини",
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

                screenState.value.children.forEach { child ->
                    ChildInfoDesk(
                        child,
                        onEdit = {
                            viewModel.setCurrentChild(it)
                            onEdit()
                        },
                        onDelete = {
                            viewModel.deleteChild(it)
                        }
                    )
                    Spacer(modifier = modifier.height(16.dp))
                }

                Row(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.resetCurrentChild()
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
                    text = "Далі"
                )
            }
        }
    }
}

@Composable
@Preview
fun ChildrenInfoPreview() {
    ChildrenInfoScreen(
        viewModel = getViewModel()
    )
}
