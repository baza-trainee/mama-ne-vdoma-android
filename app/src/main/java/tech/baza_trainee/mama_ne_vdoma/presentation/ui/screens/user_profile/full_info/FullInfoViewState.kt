package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.graphics.Bitmap
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel

data class FullInfoViewState(
    val name: String = "",
    val userAvatar: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
    val address: String = "",
    val schedule: ScheduleModel = ScheduleModel(),
    val children: List<ChildEntity> = emptyList(),
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)
