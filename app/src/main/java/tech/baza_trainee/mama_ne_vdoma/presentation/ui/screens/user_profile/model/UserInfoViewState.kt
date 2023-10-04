package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import android.graphics.Bitmap
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserInfoViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val phone: String = "",
    val phoneValid: ValidField = ValidField.EMPTY,
    val code: String = "",
    val country: String = "",
    val userAvatar: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val avatarSizeError: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)
