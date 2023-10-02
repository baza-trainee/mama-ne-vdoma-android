package tech.baza_trainee.mama_ne_vdoma.data.mapper

import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ResendCodeDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserLocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ResendCodeEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserLocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity

fun AuthUserEntity.toDataModel() = AuthUserDto(email, password)

fun ConfirmEmailEntity.toDataModel() = ConfirmEmailDto(email, code)

fun ResendCodeEntity.toDataModel() = ResendCodeDto(email)

fun UserInfoEntity.toDataModel() = UserInfoDto(name, countryCode, phone, avatar)

fun UserLocationEntity.toDataModel() = UserLocationDto(lat, lon)

fun UserProfileDto.toDomainModel() = UserProfileEntity(email, name, countryCode, phone, avatar, location.toDomainModel())

fun LocationDto.toDomainModel() = LocationEntity(type, coordinates)