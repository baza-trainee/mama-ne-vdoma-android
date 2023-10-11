package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class GroupMember(
    val name: String = "Name",
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val email: String = "name@example.com",
    val phone: String = "+380678888888"
)
