package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class EditProfileViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val phone: String = "",
    val phoneValid: ValidField = ValidField.EMPTY,
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val isEmailChanged: Boolean = false,
    val isEmailVerified: Boolean = false,
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val country: String = "",
    val userAvatar: Uri = Uri.EMPTY,
    val address: String = "",
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val note: String = "",
    val noteValid: ValidField = ValidField.EMPTY,
    val schedule: ScheduleModel = ScheduleModel(),
    val children: List<ChildEntity> = emptyList(),
    val childrenNotesValid: SnapshotStateMap<Int, ValidField> = mutableStateMapOf(),
    val isLoading: Boolean = false
)
