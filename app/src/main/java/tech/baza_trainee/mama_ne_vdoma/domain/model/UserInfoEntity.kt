package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.time.DayOfWeek

data class UserInfoEntity(
    val name: String = "",
    val countryCode: String = "",
    val phone: String = "",
    val sendingEmails: Boolean = true,
    val avatar: String? = null,
    val schedule: SnapshotStateMap<DayOfWeek, DayPeriod> = getDefaultSchedule(),
    val note: String = "",
    val deviceId: String? = null
)