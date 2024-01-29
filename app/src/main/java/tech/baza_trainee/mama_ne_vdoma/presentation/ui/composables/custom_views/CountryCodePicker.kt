package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.models.CountryCode
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.infiniteColorAnimation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodePicker(
    modifier: Modifier = Modifier,
    currentCode: String,
    currentPhone: String,
    isPhoneValid: ValidField,
    countries: List<CountryCode>,
    onCodeSelected: (CountryCode) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onKeyBoardAction: () -> Unit = {}
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var isPhoneFocused by rememberSaveable { mutableStateOf(false) }

    val isCodeHighlighted = currentCode.isEmpty()

    val color = infiniteColorAnimation(
        initialValue = Color.White,
        targetValue = Color.Red,
        duration = 1000
    )

    Column() {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .weight(0.25f)
                    .clickable {
                        openBottomSheet = true
                    },
                value = currentCode,
                label = { Text("Код") },
                onValueChange = {},
                enabled = false,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SlateGray,
                    unfocusedContainerColor = SlateGray,
                    disabledContainerColor = SlateGray,
                    focusedBorderColor = if (isCodeHighlighted) color else MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = if (isCodeHighlighted) color else MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
            )

            val focusRequester = remember { FocusRequester() }

            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isPhoneFocused = it.isFocused
                    }
                    .weight(.75f),
                value = currentPhone,
                label = { Text("Введіть свій номер телефону") },
                placeholder = { Text("Номер телефону") },
                onValueChange = onPhoneChanged,
                isError = isPhoneValid == ValidField.INVALID && isPhoneFocused,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyBoardAction() }
                ),
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    disabledBorderColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
                enabled = currentCode.isNotEmpty(),
                textStyle = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                )
            )
        }

        if (isPhoneValid == ValidField.INVALID && isPhoneFocused) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ви ввели некоректний номер",
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = redHatDisplayFontFamily,
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = 14.sp
            )
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            var searchCodeRequest by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Виберіть код",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    value = searchCodeRequest,
                    onValueChange = { searchCodeRequest = it.lowercase() },
                    label = { Text("Введіть назву країни")},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                searchCodeRequest = ""
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = "clear_search"
                            )
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .weight(1f)
                ) {
                    itemsIndexed(
                        countries
                            .filter { it.country.lowercase().contains(searchCodeRequest) }
                            .sortedBy { it.phoneCode }
                    ) { _, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .clickable {
                                    onCodeSelected(item)
                                    openBottomSheet = false
                                },
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 8.dp),
                                text = item.phoneCode,
                                fontSize = 14.sp
                            )
                            Text(
                                modifier = Modifier.weight(0.7f),
                                text = item.country,
                                fontSize = 14.sp
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}