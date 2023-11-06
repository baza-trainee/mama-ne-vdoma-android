package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun ButtonText(text: String) {
    Text(
        text = text,
        fontFamily = redHatDisplayFontFamily,
        fontWeight = FontWeight.Bold
    )
}