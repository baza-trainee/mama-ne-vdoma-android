package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ChildCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import kotlin.random.Random

@Composable
fun ChooseChildScreen(
    modifier: Modifier = Modifier,
    children: List<ChildEntity> = mutableListOf<ChildEntity>().apply {
        repeat(4) {
            add(
                ChildEntity(
                    name = "Child ${it + 1}",
                    age = Random.nextInt(it + 1).toString(),
                    gender = if (it / 2 == 0) Gender.BOY else Gender.GIRL
                )
            )
        }
    }
) {
    SurfaceWithNavigationBars {
        BackHandler { }

        ConstraintLayout(
            modifier = modifier.fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            HeaderWithToolbar(
                modifier = modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    },
                title = "Пошук групи",
                onBack = { }
            )

            var selectedChild: ChildEntity? by remember { mutableStateOf(null) }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .constrainAs(content) {
                        top.linkTo(topGuideline, 16.dp)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    }
            ) {
                Text(
                    text = "Для кого шукаємо групу?",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = modifier.height(4.dp))

                children.forEach { childEntity ->
                    ChildCard(
                        modifier = modifier.fillMaxWidth(),
                        child = childEntity,
                        isSelected = selectedChild == childEntity,
                        onSelected = { selectedChild = it }
                    )
                }

                Spacer(modifier = modifier.height(8.dp))
            }

            Button(
                modifier = modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(48.dp),
                onClick = { },
                enabled = selectedChild != null
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }

//        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ChooseChildScreenPreview() {
    ChooseChildScreen()
}