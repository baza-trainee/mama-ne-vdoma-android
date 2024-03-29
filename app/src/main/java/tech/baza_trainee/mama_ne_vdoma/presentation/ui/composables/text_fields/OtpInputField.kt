package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SlateGray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_56_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_72_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
@Preview
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String = "",
    otpCount: Int = 4,
    onOtpTextChange: (String, Boolean) -> Unit = { _,_ -> }
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldLoaded by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onGloballyPositioned {
                if (!textFieldLoaded) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                    textFieldLoaded = true
                }
            },
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    if (index < otpCount - 1)
                        Spacer(modifier = Modifier.width(size_8_dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val value = when {
        index >= text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .height(size_56_dp)
            .width(size_72_dp)
            .border(
                width = size_1_dp,
                color = when {
                    isFocused -> MaterialTheme.colorScheme.primary
                    else -> SlateGray
                },
                shape = RoundedCornerShape(size_4_dp)
            )
            .padding(size_2_dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontFamily = redHatDisplayFontFamily,
            color = if (isFocused) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            textAlign = TextAlign.Center
        )
    }
}