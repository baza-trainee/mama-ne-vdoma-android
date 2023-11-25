package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import java.time.DayOfWeek

data class GroupUiModel(
    val id: String = "",
    val adminId: String = "",
    val name: String = "",
    val description: String = "",
    val ages: String = "",
    val rating: Float = 5.0f,
    @Stable val avatar: Uri = Uri.EMPTY,
    val address: String = "",
    val location: LatLng = LatLng(0.00, 0.00),
    @Stable val members: List<MemberUiModel> = emptyList(),
    val isChecked: Boolean = false,
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule()
)

data class MemberUiModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: Uri = Uri.EMPTY,
    val rating: Float = 5.0f,
    @Stable val children: List<String> = emptyList()
)
