package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class UserProfileCommunicator {
    var name = ""
    var code = ""
    var phone = ""
    var uriForCrop = Uri.EMPTY
    var userAvatar = BitmapHelper.DEFAULT_BITMAP
    var croppedImage = BitmapHelper.DEFAULT_BITMAP
    var address = ""
    var schedule = ScheduleModel()
    var currentChildId = ""
    var isUserInfoFilled = false
    var isChildInfoFilled = false
}