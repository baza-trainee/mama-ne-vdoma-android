package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import java.time.DayOfWeek

sealed interface EditProfileEvent {
    data object OnBack: EditProfileEvent
    data object OnSaveAndBack: EditProfileEvent
    data object SaveInfo: EditProfileEvent
    data object OnEditPhoto: EditProfileEvent
    data object OnDeletePhoto: EditProfileEvent
    data object ResetUiState: EditProfileEvent
    data class ValidateUserName(val name: String) : EditProfileEvent
    data class SetImageToCrop(val uri: Uri) : EditProfileEvent
    data class ValidatePhone(val phone: String) : EditProfileEvent
    data class SetCode(val code: String, val country: String) : EditProfileEvent
    data object RequestUserLocation : EditProfileEvent
    data class OnMapClick(val location: LatLng) : EditProfileEvent
    data object GetLocationFromAddress : EditProfileEvent
    data class UpdateUserAddress(val address: String) : EditProfileEvent
    data object DeleteUser: EditProfileEvent
    data object AddChild: EditProfileEvent
    data object OnSaveAndAddChild: EditProfileEvent
    data class SaveParentInfo(val schedule: SnapshotStateMap<DayOfWeek, DayPeriod>, val note: String): EditProfileEvent
    data class DeleteChild(val id: String): EditProfileEvent
    data class SaveChildren(val schedules: Map<Int, SnapshotStateMap<DayOfWeek, DayPeriod>>, val notes: Map<Int, String>): EditProfileEvent
    data object GoToMain: EditProfileEvent
}