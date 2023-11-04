package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class GroupEntity(
    val id: String = "",
    val adminId: String = "",
    val name: String = "",
    val description: String = "",
    val ages: String = "",
    val avatar: String = "",
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val members: List<MemberEntity> = emptyList(),
    val location: LocationEntity = LocationEntity(),
    val askingJoin: List<MemberEntity> = emptyList()
)

data class MemberEntity(
    val childId: String,
    val parentId: String
)