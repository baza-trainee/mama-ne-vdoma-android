package tech.baza_trainee.mama_ne_vdoma.domain.model

import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.DayOfWeek

@Parcelize
data class ChildEntity(
    val name: String = "",
    val age: String = "",
    val gender: Gender = Gender.NONE,
    val note: String = "",
    val parentId: String = "",
    val childId: String = "",
    val schedule: @RawValue SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule()
): Parcelable
