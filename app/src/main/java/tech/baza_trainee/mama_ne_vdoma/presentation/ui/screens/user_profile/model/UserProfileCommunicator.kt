package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.domain.model.Child
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel

class UserProfileCommunicator {
    var name: String = ""
    var code: String = ""
    var phone: String = ""
    var userAvatar: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
    var address: String = ""
    var schedule: ScheduleModel = ScheduleModel()
    var children: List<Child> = emptyList()
    var currentChild: Child = Child()
}