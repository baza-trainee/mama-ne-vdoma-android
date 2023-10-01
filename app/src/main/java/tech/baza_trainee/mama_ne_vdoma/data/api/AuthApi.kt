package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tech.baza_trainee.mama_ne_vdoma.data.model.AuthUserDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ConfirmEmailDto

interface AuthApi {

    @POST("api/auth/register")
    suspend fun registerUser(@Body user: AuthUserDto): Response<Unit>

    @POST("api/auth/confirm")
    suspend fun confirmEmail(@Body confirmation: ConfirmEmailDto): Response<Unit>

    @POST("api/auth/login")
    suspend fun loginUser(@Body user: AuthUserDto): Response<Unit>

}