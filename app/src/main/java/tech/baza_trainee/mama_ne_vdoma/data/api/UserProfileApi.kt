package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import tech.baza_trainee.mama_ne_vdoma.data.model.ChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.InitChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserInfoDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserProfileResponse
import tech.baza_trainee.mama_ne_vdoma.data.model.UserSearchRequest
import tech.baza_trainee.mama_ne_vdoma.data.model.WeekScheduleDto

interface UserProfileApi {

    @GET("api/auth/me")
    suspend fun getUserInfo(): Response<UserProfileDto>

    @PATCH("api/parent")
    suspend fun saveUserInfo(@Body userInfo: UserInfoDto): Response<Unit>

    @PATCH("api/parent/geo")
    suspend fun saveUserLocation(@Body location: LocationPatchDto): Response<Unit>

    @DELETE("api/parent")
    suspend fun deleteUser(): Response<Unit>

    @GET("api/parent/id/{parentId}")
    suspend fun getUserById(@Path("parentId") parentId: String): Response<UserProfileResponse>

    @POST("api/parent/find")
    suspend fun getUserById(@Body email: UserSearchRequest): Response<UserProfileResponse>

    @POST("api/child")
    suspend fun saveChild(@Body request: InitChildDto): Response<ChildDto>

    @GET("api/child")
    suspend fun getChildren(): Response<List<ChildDto>>

    @GET("api/child/{id}")
    suspend fun getChildById(@Path("id") childId: String): Response<ChildDto>

    @PATCH("api/child/{id}")
    suspend fun patchChildById(@Path("id") childId: String, @Body data: WeekScheduleDto): Response<ChildDto>

    @DELETE("api/child/{id}")
    suspend fun deleteChildById(@Path("id") childId: String): Response<Unit>

    @DELETE("api/parent/photo")
    suspend fun deleteUserAvatar(): Response<Unit>
}