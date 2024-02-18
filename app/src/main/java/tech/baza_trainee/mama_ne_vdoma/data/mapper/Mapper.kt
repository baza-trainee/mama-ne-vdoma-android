package tech.baza_trainee.mama_ne_vdoma.data.mapper

import androidx.compose.runtime.mutableStateMapOf
import tech.baza_trainee.mama_ne_vdoma.data.model.ChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.DayScheduleDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupFullInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.JoinRequestDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.MemberDto
import tech.baza_trainee.mama_ne_vdoma.data.model.NotificationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserRatingDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.JoinRequestEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.MemberEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.NotificationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import java.time.DayOfWeek

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
    schedule = week.toSchedule(),
    groupJoinRequests = groupJoinRequests.map { it.toDomainModel() },
    notifications = notifications.map { it.toDomainModel() },
    rating = karma ?: 0f
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

fun GroupFullInfoDto.toDomainModel() = GroupFullInfoEntity(
    group = group.toDomainModel(),
    parents = parents.map { it.toDomainModel() },
    children = children.map { it.toDomainModel() }
)

fun UserRatingDto.toDomainModel() = UserRatingDomainModel(rating, message, reviewer, receiver)
