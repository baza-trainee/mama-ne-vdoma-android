package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.CreateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationPatchEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface GroupsRepository {

    suspend fun createGroup(
        childId: String,
        initialGroupInfo: CreateGroupEntity
    ): RequestResult<GroupEntity>

    suspend fun updateGroup(
        groupId: String,
        updateGroupInfo: UpdateGroupEntity
    ): RequestResult<Unit>

    suspend fun updateGroupLocation(
        groupId: String,
        location: LocationPatchEntity
    ): RequestResult<Unit>

    suspend fun getGroupsForParent(parent: String): RequestResult<List<GroupEntity>>

    suspend fun getGroupsByArea(
        lat: Double,
        lng: Double,
        radius: Int
    ): RequestResult<List<GroupEntity>>
}