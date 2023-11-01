package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.ValidateEmailDto
import tech.baza_trainee.mama_ne_vdoma.data.model.VerifyEmailDto

interface UserAuthApi {

    @GET("api/auth/me")
    suspend fun getUserInfo(): Response<UserProfileDto>

    @POST("api/auth/change-email-request")
    suspend fun changeEmailInit(@Body request: ValidateEmailDto): Response<Unit>

    @POST("api/auth/change-email")
    suspend fun changeEmail(@Body request: VerifyEmailDto): Response<Unit>
}