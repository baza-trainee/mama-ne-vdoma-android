package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface AuthRepository {

    suspend fun registerUser(user: AuthUserEntity): RequestResult<Unit>
    suspend fun confirmEmail(confirmation: ConfirmEmailEntity): RequestResult<Unit>
    suspend fun loginUser(user: AuthUserEntity): RequestResult<Unit>
}