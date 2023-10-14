package tech.baza_trainee.mama_ne_vdoma.data.mapper

import androidx.compose.runtime.mutableStateMapOf
import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.CreateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.DayScheduleDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.InitChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.MemberDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RequestWithEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RestorePasswordDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UpdateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.WeekScheduleDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.CreateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.InitChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationPatchEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.MemberEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.RequestWithEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.RestorePasswordEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import java.time.DayOfWeek

fun AuthUserEntity.toDataModel() = AuthUserDto(email, password)

fun RestorePasswordEntity.toDataModel() = RestorePasswordDto(email, code, password)

fun ConfirmEmailEntity.toDataModel() = ConfirmEmailDto(email, code)

fun RequestWithEmailEntity.toDataModel() = RequestWithEmailDto(email)

fun UserInfoEntity.toDataModel() = UserInfoDto(
    name,
    countryCode,
    phone,
    avatar,
    week = mutableMapOf<String, DayScheduleDto>().also { map ->
        schedule.schedule.forEach {
            map[it.key.name.lowercase()] = it.value.toDataModel()
        }
    }
)

fun LocationPatchEntity.toDataModel() = LocationPatchDto(lat, lon)

fun UserProfileDto.toDomainModel() = UserProfileEntity(
    id,
    email,
    name.orEmpty(),
    countryCode.orEmpty(),
    phone.orEmpty(),
    avatar.orEmpty(),
    location.toDomainModel(),
    schedule = ScheduleModel(
        schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
            if (week != null)
                week.forEach { entry ->
                    val key = DayOfWeek.valueOf(entry.key.uppercase())
                    map[key] = entry.value.toDomainModel()
                }
            else
                DayOfWeek.values().forEach {
                    map[it] = DayPeriod()
                }
        }
    )
)

fun LocationDto?.toDomainModel() = if (this != null)
        LocationEntity(type, coordinates)
    else
        LocationEntity()

fun InitChildEntity.toDataModel() = InitChildDto(name, age, isMale)

fun ChildDto.toDomainModel() = ChildEntity(
    name,
    age.toString(),
    if (isMale) Gender.BOY else Gender.GIRL,
    note.orEmpty(),
    parentId,
    childId,
    schedule = ScheduleModel(
        schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
            if (week != null)
                week.forEach { entry ->
                    val key = DayOfWeek.valueOf(entry.key.uppercase())
                    map[key] = entry.value.toDomainModel()
                }
            else
                DayOfWeek.values().forEach {
                    map[it] = DayPeriod()
                }
        }
    )
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
    week = mutableMapOf<String, DayScheduleDto>().also { map ->
        schedule.schedule.forEach {
            map[it.key.name.lowercase()] = it.value.toDataModel()
        }
    }
)

fun CreateGroupEntity.toDataModel() = CreateGroupDto(name, desc)

fun UpdateGroupEntity.toDataModel() = UpdateGroupDto(
    name = name,
    desc = desc,
    ages = ages,
    week = mutableMapOf<String, DayScheduleDto>().also { map ->
        schedule.schedule.forEach {
            map[it.key.name.lowercase()] = it.value.toDataModel()
        }
    }
)

fun MemberDto.toDomainModel() = MemberEntity(childId, parentId)

fun GroupDto.toDomainModel() = GroupEntity(
    id = id,
    name = name,
    description = desc,
    adminId = adminId,
    askingJoin = askingJoin,
    members = members.map { it.toDomainModel() }.toList(),
    ages = ages,
    location = location.toDomainModel(),
    schedule = ScheduleModel(
        schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
            if (week != null)
                week.forEach { entry ->
                    val key = DayOfWeek.valueOf(entry.key.uppercase())
                    map[key] = entry.value.toDomainModel()
                }
            else
                DayOfWeek.values().forEach {
                    map[it] = DayPeriod()
                }
        }
    )
)