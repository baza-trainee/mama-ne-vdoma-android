package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RequestWithEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RestorePasswordDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ValidateEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.VerifyEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {

    override suspend fun registerUser(email: String, password: String): RequestResult<Unit> {
        val result = authApi.registerUser(
            AuthUserDto(email, password)
        )
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun confirmEmail(email: String, code: String): RequestResult<Unit> {
        val result = authApi.confirmEmail(
            ConfirmEmailDto(email, code)
        )
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }

    override suspend fun resendCode(email: String): RequestResult<Unit> {
        val result = authApi.resendCode(RequestWithEmailDto(email))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun loginUser(email: String, password: String): RequestResult<Unit> {
        val result = authApi.loginUser(AuthUserDto(email, password))
        return if (result.isSuccessful) {
            AuthInterceptor.AUTH_TOKEN = result.body()?.jwt.orEmpty()
            RequestResult.Success(Unit)
        } else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun forgetPassword(email: String): RequestResult<Unit> {
        val result = authApi.forgetPassword(RequestWithEmailDto(email))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun resetPassword(email: String, code: String, password: String): RequestResult<Unit> {
        val result = authApi.resetPassword(RestorePasswordDto(email, code, password))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }

    override suspend fun changeEmailInit(email: String): RequestResult<Unit> {
        val auth = AuthInterceptor.bearerHeader(AuthInterceptor.AUTH_TOKEN)
        val result = authApi.changeEmailInit(auth, ValidateEmailDto(email))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun changeEmail(code: String): RequestResult<Unit> {
        val auth = AuthInterceptor.bearerHeader(AuthInterceptor.AUTH_TOKEN)
        val result = authApi.changeEmail(auth, VerifyEmailDto(code))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }
}