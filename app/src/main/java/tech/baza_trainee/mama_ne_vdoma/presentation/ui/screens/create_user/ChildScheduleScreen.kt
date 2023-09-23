package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme

@Composable
fun ChildScheduleFunc(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ChildSchedule(
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
fun ChildSchedule(
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
                modifier = modifier.fillMaxWidth()
            ) {
                val (title, name, age, gender, btnNext) = createRefs()

                val topGuideline = createGuidelineFromTop(0.2f)

                Column(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(topGuideline)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Визначіть графік",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                        text = "Вкажіть коли потрібно доглянути за вашою дитиною",
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

                val commentText = remember { mutableStateOf(TextFieldValue("")) }

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .constrainAs(age) {
                            top.linkTo(name.bottom, 16.dp)
                            bottom.linkTo(btnNext.top, 16.dp)
                        },
                    value = commentText.value,
                    label = { Text("Нотатка") },
                    onValueChange = { commentText.value = it },
                    minLines = 3,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Gray,
                        unfocusedContainerColor = Gray,
                        disabledContainerColor = Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    )
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
                    Text(text = "Встановити розклад")
                }
            }
        }
    }
}

@Composable
@Preview
fun ChildSchedulePreview() {
    ChildSchedule(
        onBack = {},
        onNext = {}
    )
}
