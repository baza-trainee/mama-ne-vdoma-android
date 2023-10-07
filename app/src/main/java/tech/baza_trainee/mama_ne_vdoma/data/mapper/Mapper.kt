package tech.baza_trainee.mama_ne_vdoma.data.mapper

import androidx.compose.runtime.mutableStateMapOf
import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.DayScheduleDto
import tech.baza_trainee.mama_ne_vdoma.data.model.InitChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RequestWithEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RestorePasswordDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserLocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.WeekScheduleDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.InitChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.RequestWithEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.RestorePasswordEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserLocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import java.time.DayOfWeek

fun AuthUserEntity.toDataModel() = AuthUserDto(email, password)

fun RestorePasswordEntity.toDataModel() = RestorePasswordDto(email, code, password)

fun ConfirmEmailEntity.toDataModel() = ConfirmEmailDto(email, code)

fun RequestWithEmailEntity.toDataModel() = RequestWithEmailDto(email)

fun UserInfoEntity.toDataModel() = UserInfoDto(name, countryCode, phone, avatar)

fun UserLocationEntity.toDataModel() = UserLocationDto(lat, lon)

fun UserProfileDto.toDomainModel() = UserProfileEntity(email, name, countryCode, phone, avatar, location.toDomainModel())

fun LocationDto.toDomainModel() = LocationEntity(type, coordinates)

fun InitChildEntity.toDataModel() = InitChildDto(name, age, isMale)

fun ChildDto.toDomainModel() = ChildEntity(
    name,
    age.toString(),
    if (isMale) Gender.BOY else Gender.GIRL,
    note.orEmpty(),
    parentId,
    childId,
    schedule = ScheduleModel(
        schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also {
            week?.forEach { entry ->
                val key = DayOfWeek.valueOf(entry.key.uppercase())
                it[key] = entry.value.toDomainModel()
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