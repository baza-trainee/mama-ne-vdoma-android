package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.CreateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class GroupsRepositoryImpl(
    private val groupsApi: GroupsApi
): GroupsRepository {
    override suspend fun createGroup(
        childId: String,
        name: String,
        description: String
    ): RequestResult<GroupEntity> {
        val result = groupsApi.createGroup(childId, CreateGroupDto(name, description))
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: GroupEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun updateGroup(
        groupId: String,
        updateGroupInfo: UpdateGroupEntity
    ): RequestResult<Unit> {
        val result = groupsApi.updateGroup(groupId, updateGroupInfo.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun updateGroupLocation(
        groupId: String,
        latitude: Double,
        longitude: Double
    ): RequestResult<Unit> {
        val result = groupsApi.updateGroupLocation(groupId, LocationPatchDto(latitude, longitude))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getGroupById(group: String): RequestResult<GroupEntity> {
        val result = groupsApi.getGroupById(group)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: GroupEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getGroupsForParent(parent: String): RequestResult<List<GroupEntity>> {
        val result = groupsApi.getGroupsForParent(parent)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.map { it.toDomainModel() }?.toList() ?: emptyList())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getGroupsByArea(
        lat: Double,
        lng: Double,
        radius: Int
    ): RequestResult<List<GroupEntity>> {
        val result = groupsApi.getGroupsByArea(lat, lng, radius)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.map { it.toDomainModel() }?.toList() ?: emptyList())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun sendJoinRequest(groupId: String, childId: String): RequestResult<Unit> {
        val result = groupsApi.joinRequest(groupId, childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun acceptRequest(groupId: String, childId: String): RequestResult<Unit> {
        val result = groupsApi.acceptRequest(groupId, childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun declineRequest(groupId: String, childId: String): RequestResult<Unit> {
        val result = groupsApi.declineRequest(groupId, childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun kickUser(groupId: String, childId: String): RequestResult<Unit> {
        val result = groupsApi.kickUser(groupId, childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun leaveGroup(groupId: String): RequestResult<Unit> {
        val result = groupsApi.leaveGroup(groupId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getGroupFullInfo(groupId: String): RequestResult<GroupFullInfoEntity> {
        val result = groupsApi.getGroupFullInfo(groupId)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: GroupFullInfoEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun cancelRequest(groupId: String, childId: String): RequestResult<Unit> {
        val result = groupsApi.cancelRequest(groupId, childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun deleteGroup(groupId: String): RequestResult<Unit> {
        val result = groupsApi.deleteGroup(groupId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun switchAdmin(groupId: String, parentId: String): RequestResult<Unit> {
        val result = groupsApi.switchAdmin(groupId, parentId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }
}