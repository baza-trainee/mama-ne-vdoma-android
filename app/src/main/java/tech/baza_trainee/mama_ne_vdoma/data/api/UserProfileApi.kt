package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserLocationDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto

interface UserProfileApi {

    @GET("api/auth/me")
    suspend fun getUserInfo(): Response<UserProfileDto>

    @PATCH("api/parent")
    suspend fun saveUserInfo(@Body userInfo: UserInfoDto): Response<Unit>

    @PATCH("api/parent/geo")
    suspend fun saveUserLocation(@Body location: UserLocationDto): Response<Unit>
}