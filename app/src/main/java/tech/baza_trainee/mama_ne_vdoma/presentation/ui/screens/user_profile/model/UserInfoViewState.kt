package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserInfoViewState(
    val nameValid: ValidField = ValidField.EMPTY,
    val phoneValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val userAvatar: Bitmap? = null
)
