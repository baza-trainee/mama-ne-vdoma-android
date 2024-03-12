package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.presentation.model.ChildUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.getDefaultSchedule
import java.time.DayOfWeek

data class FullInfoViewState(
    val name: String = "",
    val userAvatar: Uri = Uri.EMPTY,
    val address: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val children: List<ChildUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isUserInfoFilled: Boolean = false,
    val isChildInfoFilled: Boolean = false
)
