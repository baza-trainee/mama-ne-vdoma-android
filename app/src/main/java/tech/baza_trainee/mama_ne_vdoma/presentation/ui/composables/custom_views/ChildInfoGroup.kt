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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
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
            OutlinedTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_8_dp),
                value = screenState.name,
                label = stringResource(id = R.string.child_name),
                hint = stringResource(id = R.string.child_name),
                onValueChange = { handleEvent(ChildInfoEvent.ValidateChildName(it)) },
                isError = screenState.nameValid == ValidField.INVALID,
                errorText = stringResource(id = R.string.incorrect_child_name)
            )

            OutlinedTextFieldWithError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp),
                value = screenState.age,
                label = stringResource(id = R.string.child_age),
                hint = stringResource(id = R.string.child_age),
                onValueChange = { handleEvent(ChildInfoEvent.ValidateAge(it)) },
                isError = screenState.ageValid == ValidField.INVALID,
                errorText = stringResource(id = R.string.incorrect_child_age),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            val genderOptions = listOf(Gender.BOY, Gender.GIRL)

            RadioGroup(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size_16_dp)
                    .padding(horizontal = size_8_dp),
                radioGroupOptions = genderOptions,
                getText = { stringResource(id = it.gender) },
                selected = screenState.gender,
                onSelectedChange = { handleEvent(ChildInfoEvent.SetGender(it)) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(vertical = size_16_dp)
                .fillMaxWidth()
                .height(size_48_dp),
            onClick = {
                handleEvent(ChildInfoEvent.SaveChild)
            },
            enabled = screenState.nameValid == ValidField.VALID &&
                    screenState.ageValid == ValidField.VALID &&
                    screenState.gender != Gender.NONE
        ) {
            ButtonText(
                text = stringResource(id = R.string.register_child)
            )
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}