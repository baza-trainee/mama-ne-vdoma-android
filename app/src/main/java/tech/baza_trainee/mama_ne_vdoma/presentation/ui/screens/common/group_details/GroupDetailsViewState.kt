package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

@Immutable
data class GroupDetailsViewState(
    val members: List<MemberUiModel> = emptyList(),
    val userAvatar: Uri = Uri.EMPTY,
    val adminId: String = "",
    val address: String = "",
    val location: LatLng = LatLng(0.00, 0.00),
    val isAddressChecked: Boolean = true,
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val description: String = "",
    val minAge: String = "",
    val minAgeValid: ValidField = ValidField.EMPTY,
    val maxAge: String = "",
    val maxAgeValid: ValidField = ValidField.EMPTY,
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP
)
