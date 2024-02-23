package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.UserAuthApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.ValidateEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.VerifyEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.getRequestResult
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository

class UserAuthRepositoryImpl(private val userAuthApi: UserAuthApi): UserAuthRepository {

    override suspend fun getUserInfo() = userAuthApi.getUserInfo().getRequestResult {
            it.toDomainModel()
        }

    override suspend fun changeEmailInit(email: String) =
        userAuthApi.changeEmailInit(ValidateEmailDto(email)).getRequestResult()

    override suspend fun changeEmail(code: String) =
        userAuthApi.changeEmail(VerifyEmailDto(code)).getRequestResult()
}