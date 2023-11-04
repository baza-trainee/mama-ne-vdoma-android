package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import java.time.DayOfWeek

data class FullInfoViewState(
    val name: String = "",
    val userAvatar: Uri = Uri.EMPTY,
    val address: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val children: List<ChildEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isUserInfoFilled: Boolean = false,
    val isChildInfoFilled: Boolean = false
)
