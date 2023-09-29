package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ChildNameViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.RadioGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.TopBarWithArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun ChildInfoFunc(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ChildInfo(
        screenState = viewModel.childNameScreenState.collectAsStateWithLifecycle(),
        validateName = { viewModel.validateChildName(it) },
        validateAge = { viewModel.validateAge(it) },
        setGender = { viewModel.setGender(it) },
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
fun ChildInfo(
    modifier: Modifier = Modifier,
    screenState: State<ChildNameViewState> = mutableStateOf(ChildNameViewState()),
    validateName: (String) -> Unit = { },
    validateAge: (String) -> Unit = { },
    setGender: (Gender) -> Unit = { },
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
                title = "Розкажіть про свою дитину",
                onBack = onBack
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(content) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = modifier.height(16.dp))

                OutlinedTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = screenState.value.name,
                    label = "Вкажіть ім'я дитини",
                    onValueChange = { validateName(it) },
                    isError = screenState.value.nameValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректнe ім'я"
                )

                Spacer(modifier = modifier.height(16.dp))

                OutlinedTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = screenState.value.age,
                    label = "Вкажіть вік дитини",
                    onValueChange = { validateAge(it) },
                    isError = screenState.value.ageValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректний вік",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = modifier.height(16.dp))

                val genderOptions = listOf(Gender.BOY, Gender.GIRL)

                RadioGroup(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    radioGroupOptions = genderOptions,
                    getText = { it.gender },
                    selected = screenState.value.gender,
                    onSelectedChange = { setGender(it) }
                )
            }

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(48.dp),
                onClick = onNext,
                enabled = screenState.value.nameValid == ValidField.VALID &&
                        screenState.value.ageValid == ValidField.VALID &&
                        screenState.value.gender != Gender.NONE
            ) {
                ButtonText(
                    text = "Зареєструвати дитину"
                )
            }
        }
    }
}

@Composable
@Preview
fun ChildInfoPreview() {
    ChildInfo(
        onBack = {},
        onNext = {}
    )
}