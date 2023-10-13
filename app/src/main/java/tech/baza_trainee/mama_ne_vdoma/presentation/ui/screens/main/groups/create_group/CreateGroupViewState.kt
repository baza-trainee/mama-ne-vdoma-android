package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class CreateGroupViewState(
    val address: String = "",
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val description: String = "",
    val minAge: String = "",
    val minAgeValid: ValidField = ValidField.EMPTY,
    val maxAge: String = "",
    val maxAgeValid: ValidField = ValidField.EMPTY,
    val schedule: ScheduleModel = ScheduleModel(),
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val isLoading: Boolean = false
)
