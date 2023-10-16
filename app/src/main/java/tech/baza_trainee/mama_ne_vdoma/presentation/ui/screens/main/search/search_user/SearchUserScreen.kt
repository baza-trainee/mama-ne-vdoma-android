package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun SearchUserScreen(
    modifier: Modifier = Modifier,
    screenState: State<SearchUserViewState> = mutableStateOf(SearchUserViewState()),
    uiState: State<SearchUserState> = mutableStateOf(SearchUserState.Idle),
    handleEvent: (SearchUserEvent) -> Unit = {}
) {
    BackHandler { handleEvent(SearchUserEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        SearchUserState.Idle -> Unit
        is SearchUserState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(SearchUserEvent.ResetUiState)
        }

        SearchUserState.OnNothingFound -> Unit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Знайти користувача",
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

//            OutlinedTextFieldWithError(
//                text = screenState.value.name,
//                onValueChange = { handleEvent(SearchUserEvent.ValidateName(it)) },
//                modifier = Modifier.fillMaxWidth(),
//                label = "Нікнейм користувача",
//                isError = screenState.value.nameValid == ValidField.INVALID,
//                errorText = "Ви ввели некоректний нікнейм"
//            )
//
//            Text(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp),
//                text = "Нікнейм має бути від 2 до 18 символів, може містити букви кирилицею та латиницею, цифри, пробіл, дефіс",
//                fontFamily = redHatDisplayFontFamily,
//                fontSize = 12.sp
//            )
//
//            Spacer(modifier = modifier.height(16.dp))

            OutlinedTextFieldWithError(
                modifier = modifier.fillMaxWidth(),
                text = screenState.value.email,
                label = "Email користувача",
                onValueChange = { handleEvent(SearchUserEvent.ValidateEmail(it)) },
                isError = screenState.value.emailValid == ValidField.INVALID,
                errorText = "Ви ввели некоректний email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                }
            )

//            Spacer(modifier = modifier.height(24.dp))
//
//            Text(
//                modifier = modifier.fillMaxWidth(),
//                text = "Заповнення одного із двох полів обов'язкове, або можна заповнити обидва",
//                fontFamily = redHatDisplayFontFamily,
//                fontSize = 12.sp,
//                textDecoration = TextDecoration.Underline
//            )
        }

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchUserEvent.OnSearch)
            },
            enabled = /*screenState.value.nameValid == ValidField.VALID ||*/
                    screenState.value.emailValid == ValidField.VALID
        ) {
            ButtonText(
                text = "Розпочати пошук"
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SearchUserScreenPreview() {
    SearchUserScreen()
}