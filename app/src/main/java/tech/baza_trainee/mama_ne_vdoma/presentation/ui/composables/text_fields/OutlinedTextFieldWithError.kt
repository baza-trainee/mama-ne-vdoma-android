package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun PasswordTextFieldWithError(
    modifier: Modifier = Modifier,
    label: String = "Введіть свій пароль",
    placeholder: String = "Пароль",
    password: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    errorText: String = "Пароль не відповідає вимогам",
    imeAction: ImeAction = ImeAction.Default,
    onImeActionPerformed: () -> Unit = {}
) {
    Column {
        var isPasswordFocused by remember { mutableStateOf(false) }

        ShowHidePasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            placeHolder = placeholder,
            password = password,
            onValueChange = { onValueChange(it) },
            isError = isError && isPasswordFocused,
            onFocusChanged = { isPasswordFocused = it },
            imeAction = imeAction,
            onImeActionPerformed = onImeActionPerformed
        )
        if (isError && isPasswordFocused) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun OutlinedTextFieldWithError(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    hint: String = "",
    trailingIcon: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    errorText: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(
        verticalArrangement = Arrangement.Top
    ) {
        var isFieldFocused by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier
                .onFocusChanged {
                    isFieldFocused = it.isFocused
                }
                .fillMaxWidth(),
            value = value,
            label = { Text(label) },
            placeholder = { Text(hint) },
            onValueChange = { onValueChange(it) },
            isError = isError && isFieldFocused,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon ?: {
                if (isError)
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = Color.Red
                    )
            },
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(
                fontFamily = redHatDisplayFontFamily
            ),
            minLines = minLines,
            maxLines = maxLines,
            keyboardActions = keyboardActions
        )
        if (isError && isFieldFocused) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = 14.sp
            )
        }
    }
}