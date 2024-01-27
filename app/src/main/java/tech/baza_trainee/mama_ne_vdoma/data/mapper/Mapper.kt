package tech.baza_trainee.mama_ne_vdoma.data.mapper

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.data.model.ChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.DayScheduleDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupFullInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.JoinRequestDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.MemberDto
import tech.baza_trainee.mama_ne_vdoma.data.model.NotificationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UpdateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.WeekScheduleDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.JoinRequestEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.MemberEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.NotificationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import java.time.DayOfWeek

fun UserInfoEntity.toDataModel() = UserInfoDto(
    name = name,
    countryCode = countryCode,
    phone = phone,
    sendingEmails = sendingEmails,
    avatar = avatar,
    week = mutableMapOf<String, DayScheduleDto>().also { map ->
        schedule.forEach {
            map[it.key.name.lowercase()] = it.value.toDataModel()
        }
    },
    note = note.ifEmpty { null },
    deviceId = deviceId
)

fun UserProfileDto.toDomainModel() = UserProfileEntity(
    id = id,
    email =  email,
    name = name.orEmpty(),
    countryCode = countryCode.orEmpty(),
    phone = phone.orEmpty(),
    note = note.orEmpty(),
    sendingEmails = sendingEmails,
    avatar = avatar.orEmpty(),
    location = location.toDomainModel(),
    schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
            if (week != null)
                week.forEach { entry ->
                    val key = DayOfWeek.valueOf(entry.key.uppercase())
                    map[key] = entry.value.toDomainModel()
                }
            else
                DayOfWeek.entries.forEach {
                    map[it] = DayPeriod()
                }
        },
    groupJoinRequests = groupJoinRequests.map { it.toDomainModel() },
    notifications = notifications.map { it.toDomainModel() }
)

fun JoinRequestDto.toDomainModel() = JoinRequestEntity(groupId, childId)

fun NotificationDto.toDomainModel() = NotificationEntity(groupId, notificationType)

fun LocationDto?.toDomainModel() = if (this != null)
        LocationEntity(type, coordinates)
    else
        LocationEntity()

fun ChildDto.toDomainModel() = ChildEntity(
    name = name,
    age = age.toString(),
    gender = if (isMale) Gender.BOY else Gender.GIRL,
    note = note.orEmpty(),
    parentId = parentId,
    childId = childId,
    schedule = week.toSchedule()
)

fun DayScheduleDto.toDomainModel() = DayPeriod(
    morning =  if (morning && lunch && evening) false else morning,
    noon =  if (morning && lunch && evening) false else lunch,
    afternoon =  if (morning && lunch && evening) false else evening,
    wholeDay = morning && lunch && evening
)

fun DayPeriod.toDataModel() = DayScheduleDto(
    morning = if (wholeDay) true else morning,
    lunch = if (wholeDay) true else noon,
    evening = if (wholeDay) true else afternoon
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

fun MemberDto.toDomainModel() = MemberEntity(childId, parentId)

fun GroupDto.toDomainModel() = GroupEntity(
    id = id,
    name = name,
    description = desc,
    adminId = adminId,
    askingJoin = askingJoin.map { it.toDomainModel() }.toList(),
    members = members.map { it.toDomainModel() }.toList(),
    ages = ages,
    avatar = avatar.orEmpty(),
    location = location.toDomainModel(),
    schedule = week.toSchedule()
)

fun Map<String, DayScheduleDto>?.toSchedule() = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
    if (this != null)
        forEach { entry ->
            val key = DayOfWeek.valueOf(entry.key.uppercase())
            map[key] = entry.value.toDomainModel()
        }
    else
        DayOfWeek.entries.forEach {
            map[it] = DayPeriod()
        }
}

fun SnapshotStateMap<DayOfWeek, DayPeriod>.toWeek() = mutableMapOf<String, DayScheduleDto>().also { map ->
    forEach {
        map[it.key.name.lowercase()] = it.value.toDataModel()
    }
}



fun GroupFullInfoDto.toDomainModel() = GroupFullInfoEntity(
    group = group.toDomainModel(),
    parents = parents.map { it.toDomainModel() },
    children = children.map { it.toDomainModel() }
)