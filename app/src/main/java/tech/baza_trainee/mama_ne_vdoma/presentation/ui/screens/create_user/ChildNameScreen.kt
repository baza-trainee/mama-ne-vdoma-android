package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RadioGroup

@Composable
fun ChildNameFunc(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ChildName(
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
fun ChildName(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = modifier
                    .imePadding()
                    .fillMaxWidth()
            ) {
                val (topBar, name, age, gender, btnNext) = createRefs()

                val topGuideline = createGuidelineFromTop(0.2f)

                Column(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .constrainAs(topBar) {
                            top.linkTo(parent.top)
                            bottom.linkTo(topGuideline)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = modifier
                                .clickable {
                                    onBack()
                                }
                                .padding(start = 16.dp),
                            text = "<",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            modifier = modifier
                                .padding(horizontal = 24.dp)
                                .padding(bottom = 8.dp),
                            text = "Розкажіть про свою дитину",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp, top = 8.dp),
                        text = "Це допоможе підібрати для вас групи " +
                                "з дітьми приблизно одного віку",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }


                val nameText = remember { mutableStateOf(TextFieldValue("")) }

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .constrainAs(name) {
                            top.linkTo(topGuideline, 24.dp)
                        },
                    value = nameText.value,
                    label = { Text("Вкажіть ім'я дитини") },
                    onValueChange = { nameText.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Gray,
                        unfocusedContainerColor = Gray,
                        disabledContainerColor = Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    )
                )

                val ageText = remember { mutableStateOf(TextFieldValue("")) }

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .constrainAs(age) {
                            top.linkTo(name.bottom, 16.dp)
                        },
                    value = ageText.value,
                    label = { Text("Вкажіть вік дитини") },
                    onValueChange = { ageText.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Gray,
                        unfocusedContainerColor = Gray,
                        disabledContainerColor = Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    )
                )

                val genderOptions = listOf(Gender.BOY, Gender.GIRL)

                RadioGroup(
                    modifier = modifier
                        .constrainAs(gender) {
                            top.linkTo(age.bottom, 24.dp)
                        }
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    radioGroupOptions = genderOptions,
                    getText = { it.gender }
                )

                Button(
                    modifier = modifier
                        .constrainAs(btnNext) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(48.dp),
                    onClick = onNext
                ) {
                    Text(text = "Зареєструвати дитину")
                }
            }
        }
    }
}

@Composable
@Preview
fun ChildNamePreview() {
    ChildName(
        onBack = {},
        onNext = {}
    )
}
