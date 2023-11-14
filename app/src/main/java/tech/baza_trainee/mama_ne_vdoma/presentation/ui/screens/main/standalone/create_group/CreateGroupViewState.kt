package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

data class CreateGroupViewState(
    @Stable val userAvatar: Uri = Uri.EMPTY,
    val address: String = "",
    @Stable val location: LatLng = LatLng(0.00, 0.00),
    val isAddressChecked: Boolean = true,
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val description: String = "",
    val minAge: String = "",
    val minAgeValid: ValidField = ValidField.EMPTY,
    val maxAge: String = "",
    val maxAgeValid: ValidField = ValidField.EMPTY,
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val isLoading: Boolean = false
)
