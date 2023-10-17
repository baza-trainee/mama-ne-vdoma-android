package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.graphics.Bitmap
import android.net.Uri

sealed interface UserInfoEvent {
    data object OnBack: UserInfoEvent
    data object SaveInfo: UserInfoEvent
    data object OnEditPhoto: UserInfoEvent
    data object OnDeletePhoto: UserInfoEvent
    data object ResetUiState: UserInfoEvent
    data class ValidateUserName(val name: String) : UserInfoEvent
    data class SetImageToCrop(val uri: Uri) : UserInfoEvent
    data class SetCroppedImage(val bitmap: Bitmap) : UserInfoEvent
    data class ValidatePhone(val phone: String) : UserInfoEvent
    data class SetCode(val code: String, val country: String) : UserInfoEvent
}