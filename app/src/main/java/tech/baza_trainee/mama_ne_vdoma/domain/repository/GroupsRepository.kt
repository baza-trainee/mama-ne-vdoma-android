package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface GroupsRepository {

    suspend fun createGroup(
        childId: String,
        name: String,
        description: String
    ): RequestResult<GroupEntity>

    suspend fun updateGroup(
        groupId: String,
        updateGroupInfo: UpdateGroupEntity
    ): RequestResult<Unit>

    suspend fun updateGroupLocation(
        groupId: String,
        latitude: Double,
        longitude: Double
    ): RequestResult<Unit>

    suspend fun getGroupsForParent(parent: String): RequestResult<List<GroupEntity>>

    suspend fun getGroupById(group: String): RequestResult<GroupEntity>

    suspend fun getGroupsByArea(
        lat: Double,
        lng: Double,
        radius: Int
    ): RequestResult<List<GroupEntity>>

    suspend fun sendJoinRequest(groupId: String, childId: String) : RequestResult<Unit>
    suspend fun acceptRequest(groupId: String, childId: String) : RequestResult<Unit>
    suspend fun declineRequest(groupId: String, childId: String) : RequestResult<Unit>
    suspend fun kickUser(groupId: String, childId: String) : RequestResult<Unit>
    suspend fun leaveGroup(groupId: String) : RequestResult<Unit>
}