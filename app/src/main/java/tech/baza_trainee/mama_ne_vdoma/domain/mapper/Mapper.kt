package tech.baza_trainee.mama_ne_vdoma.domain.mapper

import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.data.model.DayScheduleDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UpdateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserRatingDto
import tech.baza_trainee.mama_ne_vdoma.data.model.WeekScheduleDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import java.time.DayOfWeek

fun UserRatingDomainModel.toDataModel() = UserRatingDto(rating, message, reviewer, receiver)

fun UserInfoEntity.toDataModel() = UserInfoDto(
    name = name,
    countryCode = countryCode,
    phone = phone,
    sendingEmails = sendingEmails,
    avatar = avatar,
    week = schedule.toWeek(),
    note = note.ifEmpty { null },
    deviceId = deviceId
)

fun PatchChildEntity.toDataModel() = WeekScheduleDto(
    note = comment.ifEmpty { null },
    week = schedule.toWeek()
)

fun UpdateGroupEntity.toDataModel() = UpdateGroupDto(
    name = name,
    desc = desc,
    ages = ages,
    avatar = avatar,
    week = schedule.toWeek()
)

fun DayPeriod.toDataModel() = DayScheduleDto(
    morning = if (wholeDay) true else morning,
    lunch = if (wholeDay) true else noon,
    evening = if (wholeDay) true else afternoon
)

fun SnapshotStateMap<DayOfWeek, DayPeriod>.toWeek() = mutableMapOf<String, DayScheduleDto>().also { map ->
    forEach {
        map[it.key.name.lowercase()] = it.value.toDataModel()
    }
}
