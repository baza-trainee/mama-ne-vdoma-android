package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

@Stable
data class EditProfileViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val phone: String = "",
    val phoneValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val country: String = "",
    val userAvatar: Uri = Uri.EMPTY,
    val address: String = "",
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val isAddressChecked: Boolean = true,
    val note: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val children: List<ChildEntity> = emptyList(),
    val childrenNotesValid: SnapshotStateMap<Int, ValidField> = mutableStateMapOf(),
    val isLoading: Boolean = false
)
