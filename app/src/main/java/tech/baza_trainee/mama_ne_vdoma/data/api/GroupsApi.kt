package tech.baza_trainee.mama_ne_vdoma.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import tech.baza_trainee.mama_ne_vdoma.data.model.CreateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.GroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UpdateGroupDto

interface GroupsApi {

    @POST("api/group/{childId}")
    suspend fun createGroup(@Path("childId") childId: String, @Body initialGroupInfo: CreateGroupDto): Response<GroupDto>

    @PATCH("api/group/{groupId}")
    suspend fun updateGroup(@Path("groupId") groupId: String, @Body updateGroupInfo: UpdateGroupDto): Response<Unit>

    @PATCH("api/group/geo/{groupId}")
    suspend fun updateGroupLocation(@Path("groupId") groupId: String, @Body location: LocationPatchDto): Response<Unit>
}