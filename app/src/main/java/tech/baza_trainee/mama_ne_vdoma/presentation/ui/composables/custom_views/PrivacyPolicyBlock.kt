package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun PrivacyPolicyBlock(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChanged
        )
        Column(
            modifier = Modifier.fillMaxWidth(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Даю згоду на обробку моїх персональних даних, а також ознайомлена та погоджуюсь із:",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.clickable {  },
                text = "-   Політикою конфіденційності;",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.clickable {  },
                text = "-   Умовами використання мобільного застосунку “Мама-не-вдома”;",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.clickable {  },
                text = "-   Відмовою від відповідальності",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        }
    }
}