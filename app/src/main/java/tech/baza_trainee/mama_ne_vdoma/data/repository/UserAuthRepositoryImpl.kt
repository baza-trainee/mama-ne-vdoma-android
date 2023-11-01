package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.UserAuthApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.ValidateEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.VerifyEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class UserAuthRepositoryImpl(private val userAuthApi: UserAuthApi): UserAuthRepository {

    override suspend fun getUserInfo(): RequestResult<UserProfileEntity> {
        val result = userAuthApi.getUserInfo()
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: UserProfileEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }

    override suspend fun changeEmailInit(email: String): RequestResult<Unit> {
        val result = userAuthApi.changeEmailInit(ValidateEmailDto(email))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun changeEmail(code: String): RequestResult<Unit> {
        val result = userAuthApi.changeEmail(VerifyEmailDto(code))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }
}