package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RequestWithEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.RestorePasswordDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.getRequestResult
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi
): AuthRepository {

    override suspend fun registerUser(email: String, password: String) =
        authApi.registerUser(AuthUserDto(email, password)).getRequestResult()

    override suspend fun confirmEmail(email: String, code: String) =
        authApi.confirmEmail(ConfirmEmailDto(email, code)).getRequestResult()

    override suspend fun resendCode(email: String) =
        authApi.resendCode(RequestWithEmailDto(email)).getRequestResult()

    override suspend fun loginUser(email: String, password: String) =
        authApi.loginUser(AuthUserDto(email, password)).getRequestResult {
            it.id
        }

    override suspend fun forgetPassword(email: String) =
        authApi.forgetPassword(RequestWithEmailDto(email)).getRequestResult()

    override suspend fun resetPassword(email: String, code: String, password: String) =
        authApi.resetPassword(RestorePasswordDto(email, code, password)).getRequestResult()

    override suspend fun signupWithGoogle(code: String) =
        authApi.signupWithGoogle(code).getRequestResult {
            it.id
        }
}