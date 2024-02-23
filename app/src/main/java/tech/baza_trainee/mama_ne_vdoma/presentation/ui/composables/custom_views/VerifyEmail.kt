package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OtpTextField
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp

@Composable
fun VerifyEmail(
    modifier: Modifier = Modifier,
    otp: String,
    isOtpValid: Boolean,
    onVerify: (String, Boolean) -> Unit,
    onResend: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.verify_email_info),
            fontSize = font_size_16_sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = redHatDisplayFontFamily
        )

        OtpTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_32_dp),
            otpText = otp,
            onOtpTextChange = { value, otpInputFilled ->
                onVerify(value, otpInputFilled)
            }
        )
        if (!isOtpValid)
            Text(
                modifier = Modifier.padding(top = size_4_dp),
                text = stringResource(id = R.string.wrong_otp),
                color = Color.Red,
                fontFamily = redHatDisplayFontFamily,
                style = TextStyle(
                    fontFamily = redHatDisplayFontFamily
                ),
                fontSize = font_size_14_sp
            )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(vertical = size_16_dp)
                .fillMaxWidth()
                .height(size_48_dp),
            onClick = onResend
        ) {
            ButtonText(
                text = stringResource(id = R.string.send_code_again)
            )
        }
    }
}

@Composable
@Preview
fun VerifyEmailPreview() {
    VerifyEmail(
        otp = "",
        isOtpValid = false,
        onVerify = { _, _ -> },
        onResend = {}
    )
}