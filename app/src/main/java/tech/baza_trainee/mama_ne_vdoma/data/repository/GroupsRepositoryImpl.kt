package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.model.CreateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationPatchEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class GroupsRepositoryImpl(
    private val groupsApi: GroupsApi
): GroupsRepository {
    override suspend fun createGroup(
        childId: String,
        initialGroupInfo: CreateGroupEntity
    ): RequestResult<GroupEntity> {
        val result = groupsApi.createGroup(childId, initialGroupInfo.toDataModel())
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
        location: LocationPatchEntity
    ): RequestResult<Unit> {
        val result = groupsApi.updateGroupLocation(groupId, location.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getGroupsForParent(parent: String): RequestResult<List<GroupEntity>> {
        val result = groupsApi.getGroupsForParent(parent)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.map { it.toDomainModel() }?.toList() ?: emptyList())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }
}