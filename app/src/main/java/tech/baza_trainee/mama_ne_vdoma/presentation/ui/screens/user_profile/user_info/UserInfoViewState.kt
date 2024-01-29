package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.graphics.Bitmap
import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.models.CountryCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserInfoViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val phone: String = "",
    val phoneValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val country: String = "",
    val userAvatar: Uri = Uri.EMPTY,
    val bitmapAvatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val isLoading: Boolean = false,
    val countries: List<CountryCode> = emptyList()
)
