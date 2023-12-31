package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun ChildInfoGroup(
    modifier: Modifier = Modifier,
    screenState: ChildInfoViewState = ChildInfoViewState(),
    handleEvent: (ChildInfoEvent) -> Unit = { _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                value = screenState.name,
                label = "Ім'я дитини",
                hint = "Ім'я дитини",
                onValueChange = { handleEvent(ChildInfoEvent.ValidateChildName(it)) },
                isError = screenState.nameValid == ValidField.INVALID,
                errorText = "Ви ввели некоректнe ім'я"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                value = screenState.age,
                label = "Вік дитини",
                hint = "Вік дитини",
                onValueChange = { handleEvent(ChildInfoEvent.ValidateAge(it)) },
                isError = screenState.ageValid == ValidField.INVALID,
                errorText = "Ви ввели некоректний вік",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val genderOptions = listOf(Gender.BOY, Gender.GIRL)

            RadioGroup(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                radioGroupOptions = genderOptions,
                getText = { it.gender },
                selected = screenState.gender,
                onSelectedChange = { handleEvent(ChildInfoEvent.SetGender(it)) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                handleEvent(ChildInfoEvent.SaveChild)
            },
            enabled = screenState.nameValid == ValidField.VALID &&
                    screenState.ageValid == ValidField.VALID &&
                    screenState.gender != Gender.NONE
        ) {
            ButtonText(
                text = "Зареєструвати дитину"
            )
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}