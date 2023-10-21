package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import java.time.DayOfWeek

sealed interface EditProfileEvent {
    data object OnBack: EditProfileEvent
    data object SaveInfo: EditProfileEvent
    data object OnEditPhoto: EditProfileEvent
    data object OnDeletePhoto: EditProfileEvent
    data object ResetUiState: EditProfileEvent
    data object VerifyEmail: EditProfileEvent
    data class ValidateUserName(val name: String) : EditProfileEvent
    data class SetImageToCrop(val uri: Uri) : EditProfileEvent
    data class ValidatePhone(val phone: String) : EditProfileEvent
    data class SetCode(val code: String, val country: String) : EditProfileEvent
    data class ValidateEmail(val email: String) : EditProfileEvent
    data class ValidatePassword(val password: String) : EditProfileEvent
    data object RequestUserLocation : EditProfileEvent
    data class OnMapClick(val location: LatLng) : EditProfileEvent
    data object GetLocationFromAddress : EditProfileEvent
    data class UpdateUserAddress(val address: String) : EditProfileEvent
    data class UpdatePolicyCheck(val isChecked: Boolean) : EditProfileEvent
    data object EditUser: EditProfileEvent
    data object DeleteUser: EditProfileEvent
    data object AddChild: EditProfileEvent
    data class EditParentSchedule(val dayOfWeek: DayOfWeek, val period: Period): EditProfileEvent
    data class EditChild(val id: String): EditProfileEvent
    data class DeleteChild(val id: String): EditProfileEvent
    data class EditParentNote(val note: String): EditProfileEvent
    data class EditChildSchedule(val child: Int, val dayOfWeek: DayOfWeek, val period: Period): EditProfileEvent
    data class EditChildNote(val child: Int, val note: String) : EditProfileEvent
    data class RestoreChild(val child: Int) : EditProfileEvent
    data object SaveChildren: EditProfileEvent
    data object RestoreParentInfo: EditProfileEvent
    data object SaveParentInfo: EditProfileEvent
}