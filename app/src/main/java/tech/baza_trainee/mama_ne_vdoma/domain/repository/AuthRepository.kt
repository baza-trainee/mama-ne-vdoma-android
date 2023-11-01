package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface AuthRepository {

    suspend fun registerUser(email: String, password: String): RequestResult<Unit>

    suspend fun confirmEmail(email: String, code: String): RequestResult<Unit>

    suspend fun resendCode(email: String): RequestResult<Unit>

    suspend fun loginUser(email: String, password: String): RequestResult<Unit>

    suspend fun forgetPassword(email: String): RequestResult<Unit>

    suspend fun resetPassword(email: String, code: String, password: String): RequestResult<Unit>

    suspend fun signupWithGoogle(code: String): RequestResult<Unit>
}