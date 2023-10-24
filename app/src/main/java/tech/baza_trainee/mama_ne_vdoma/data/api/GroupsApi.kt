package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tech.baza_trainee.mama_ne_vdoma.data.model.CreateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UpdateGroupDto

interface GroupsApi {

    @POST("api/group/{childId}")
    suspend fun createGroup(
        @Path("childId") childId: String,
        @Body initialGroupInfo: CreateGroupDto
    ): Response<GroupDto>

    @PATCH("api/group/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: String,
        @Body updateGroupInfo: UpdateGroupDto
    ): Response<Unit>

    @GET("api/group/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String
    ): Response<GroupDto>

    @PATCH("api/group/geo/{groupId}")
    suspend fun updateGroupLocation(
        @Path("groupId") groupId: String,
        @Body location: LocationPatchDto
    ): Response<Unit>

    @GET("api/group/find/{parentId}")
    suspend fun getGroupsForParent(@Path("parentId") parentId: String): Response<List<GroupDto>>

    @GET("api/group/find/geo")
    suspend fun getGroupsByArea(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("radius") radius: Int
    ): Response<List<GroupDto>>

    @POST("api/group/join/{groupId}/{childId}")
    suspend fun joinRequest(@Path("groupId") groupId: String, @Path("childId") childId: String): Response<Unit>

    @POST("api/group/accept/{groupId}/{childId}")
    suspend fun acceptRequest(@Path("groupId") groupId: String, @Path("childId") childId: String): Response<Unit>

    @POST("api/group/decline/{groupId}/{childId}")
    suspend fun declineRequest(@Path("groupId") groupId: String, @Path("childId") childId: String): Response<Unit>

    @POST("api/group/kick/{groupId}/{childId}")
    suspend fun kickUser(@Path("groupId") groupId: String, @Path("childId") childId: String): Response<Unit>
}