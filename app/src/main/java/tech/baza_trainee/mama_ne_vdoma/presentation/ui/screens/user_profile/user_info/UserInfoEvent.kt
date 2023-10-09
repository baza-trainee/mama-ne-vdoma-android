package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.net.Uri

sealed interface UserInfoEvent {
    object OnBack: UserInfoEvent
    object SaveInfo: UserInfoEvent
    object OnEditPhoto: UserInfoEvent
    object OnDeletePhoto: UserInfoEvent
    object ResetUiState: UserInfoEvent
    data class ValidateUserName(val name: String) : UserInfoEvent
    data class SetImageToCrop(val uri: Uri) : UserInfoEvent
    data class ValidatePhone(val phone: String) : UserInfoEvent
    data class SetCode(val code: String, val country: String) : UserInfoEvent
}