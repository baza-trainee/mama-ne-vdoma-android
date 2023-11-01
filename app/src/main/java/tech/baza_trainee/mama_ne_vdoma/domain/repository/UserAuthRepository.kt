package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface UserAuthRepository {

    suspend fun getUserInfo(): RequestResult<UserProfileEntity>
    suspend fun changeEmailInit(email: String): RequestResult<Unit>
    suspend fun changeEmail(code: String): RequestResult<Unit>
}