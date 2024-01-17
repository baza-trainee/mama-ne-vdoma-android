package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.CreateGroupDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.utils.getRequestResult
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository

class GroupsRepositoryImpl(
    private val groupsApi: GroupsApi
): GroupsRepository {
    override suspend fun createGroup(
        childId: String,
        name: String,
        description: String
    ) = groupsApi.createGroup(childId, CreateGroupDto(name, description)).getRequestResult {
        it?.toDomainModel() ?: GroupEntity()
    }

    override suspend fun updateGroup(
        groupId: String,
        updateGroupInfo: UpdateGroupEntity
    ) = groupsApi.updateGroup(groupId, updateGroupInfo.toDataModel()).getRequestResult()

    override suspend fun updateGroupLocation(
        groupId: String,
        latitude: Double,
        longitude: Double
    ) = groupsApi.updateGroupLocation(groupId, LocationPatchDto(latitude, longitude))
        .getRequestResult()

    override suspend fun getGroupById(group: String) =
        groupsApi.getGroupById(group).getRequestResult {
            it?.toDomainModel() ?: GroupEntity()
        }

    override suspend fun getGroupsForParent(parent: String) =
        groupsApi.getGroupsForParent(parent).getRequestResult { list ->
            list?.map { it.toDomainModel() }?.toList() ?: emptyList()
        }

    override suspend fun getGroupsByArea(
        lat: Double,
        lng: Double,
        radius: Int
    ) = groupsApi.getGroupsByArea(lat, lng, radius).getRequestResult { list ->
        list?.map { it.toDomainModel() }?.toList() ?: emptyList()
    }

    override suspend fun sendJoinRequest(groupId: String, childId: String) =
        groupsApi.joinRequest(groupId, childId).getRequestResult()

    override suspend fun acceptRequest(groupId: String, childId: String) =
        groupsApi.acceptRequest(groupId, childId).getRequestResult()

    override suspend fun declineRequest(groupId: String, childId: String) =
        groupsApi.declineRequest(groupId, childId).getRequestResult()

    override suspend fun kickUser(groupId: String, childId: String) =
        groupsApi.kickUser(groupId, childId).getRequestResult()

    override suspend fun leaveGroup(groupId: String) =
        groupsApi.leaveGroup(groupId).getRequestResult()

    override suspend fun getGroupFullInfo(groupId: String) =
        groupsApi.getGroupFullInfo(groupId).getRequestResult {
            it?.toDomainModel() ?: GroupFullInfoEntity()
        }

    override suspend fun cancelRequest(groupId: String, childId: String) =
        groupsApi.cancelRequest(groupId, childId).getRequestResult()

    override suspend fun deleteGroup(groupId: String) =
        groupsApi.deleteGroup(groupId).getRequestResult()

    override suspend fun switchAdmin(groupId: String, parentId: String) =
        groupsApi.switchAdmin(groupId, parentId).getRequestResult()
}