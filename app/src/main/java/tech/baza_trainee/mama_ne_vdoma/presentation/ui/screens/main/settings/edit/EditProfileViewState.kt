package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.models.CountryCode
import tech.baza_trainee.mama_ne_vdoma.presentation.model.ChildUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.getDefaultSchedule
import java.time.DayOfWeek

@Immutable
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
    val tempNote: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val tempSchedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val children: List<ChildUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val countries: List<CountryCode> = emptyList(),
    val selectedChild: Int = 0,
    val childrenTempSchedules: SnapshotStateMap<Int, SnapshotStateMap<DayOfWeek, DayPeriod>> = mutableStateMapOf(),
    val childrenTempNotes: SnapshotStateMap<Int, String> = mutableStateMapOf()
)
