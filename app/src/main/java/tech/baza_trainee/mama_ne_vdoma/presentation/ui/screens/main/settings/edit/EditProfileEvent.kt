package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri

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
}