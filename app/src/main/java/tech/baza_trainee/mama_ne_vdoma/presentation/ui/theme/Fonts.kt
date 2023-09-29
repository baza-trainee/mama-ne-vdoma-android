package tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import tech.baza_trainee.mama_ne_vdoma.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val redHatDisplayFont = GoogleFont("Red Hat Display")

val redHatDisplayFontFamily = FontFamily(
    Font(googleFont = redHatDisplayFont, fontProvider = provider)
)