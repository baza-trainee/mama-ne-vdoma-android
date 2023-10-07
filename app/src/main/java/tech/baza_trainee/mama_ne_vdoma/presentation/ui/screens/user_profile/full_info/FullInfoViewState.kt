package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class FullInfoViewState(
    val name: String = "",
    val userAvatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val address: String = "",
    val schedule: ScheduleModel = ScheduleModel(),
    val children: List<ChildEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isUserInfoFilled: Boolean = false,
    val isChildInfoFilled: Boolean = false
)
