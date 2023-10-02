package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.net.Uri

sealed interface UserInfoEvent {
    object ConsumeRequestError: UserInfoEvent
    object SaveInfo: UserInfoEvent
    data class ValidateUserName(val name: String) : UserInfoEvent
    data class SetUriForCrop(val uri: Uri) : UserInfoEvent
    data class ValidatePhone(val phone: String) : UserInfoEvent
    data class SetCode(val code: String) : UserInfoEvent
}