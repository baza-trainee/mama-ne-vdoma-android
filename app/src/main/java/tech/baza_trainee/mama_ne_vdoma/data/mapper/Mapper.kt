package tech.baza_trainee.mama_ne_vdoma.data.mapper

import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity

fun AuthUserEntity.toDataModel() = AuthUserDto(email, password)

fun ConfirmEmailEntity.toDataModel() = ConfirmEmailDto(email, code)