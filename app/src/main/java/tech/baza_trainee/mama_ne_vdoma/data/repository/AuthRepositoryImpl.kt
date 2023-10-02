package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {
    override suspend fun registerUser(user: AuthUserEntity): RequestResult<Unit> {
        val result = authApi.registerUser(user.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun confirmEmail(confirmation: ConfirmEmailEntity): RequestResult<Unit> {
        val result = authApi.confirmEmail(confirmation.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun loginUser(user: AuthUserEntity): RequestResult<Unit> {
        val result = authApi.loginUser(user.toDataModel())
        return if (result.isSuccessful) {
            AuthInterceptor.AUTH_TOKEN = result.body()?.jwt.orEmpty()
            RequestResult.Success(Unit)
        } else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }
}