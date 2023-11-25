package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

interface GroupsInteractor {

    val groupsFlow: StateFlow<List<GroupUiModel>>

    fun setGroupsCoroutineScope(coroutineScope: CoroutineScope)

    fun setGroupsNetworkListener(listener: NetworkEventsListener)

    fun startFetching(entities: List<GroupEntity>, isMine: Boolean = false)

    fun getGroups(parent: String, onSuccess: (List<GroupEntity>) -> Unit)

    fun leaveGroup(group: String, onSuccess: () -> Unit)

    fun deleteGroup(group: String, onSuccess: () -> Unit)

    fun kickUser(group: String, childId: String, onSuccess: () -> Unit)

    fun switchAdmin(group: String, member: String, onSuccess: () -> Unit)
    fun updateGroupLocation(location: LatLng, groupId: String, onSuccess: () -> Unit)
    fun createGroup(
        child: String,
        name: String,
        description: String,
        onSuccess: (GroupEntity) -> Unit
    )

    fun updateGroup(
        group: String,
        name: String,
        description: String,
        ages: String,
        avatar: String,
        schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
        onSuccess: () -> Unit
    )

    fun getGroupLocation(address: String, onSuccess: (LatLng?) -> Unit)
    fun uploadGroupAvatar(image: Bitmap, onSuccess: (String) -> Unit)
    fun getGroupAddress(latLng: LatLng, onSuccess: (String) -> Unit)
}

class GroupsInteractorImpl(
    private val groupsRepository: GroupsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val filesRepository: FilesRepository
) : GroupsInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener

    private val avatarsFlow: MutableStateFlow<Pair<String, Uri>> = MutableStateFlow("" to Uri.EMPTY)
    private val avatarsMembersFlow: MutableStateFlow<Triple<String, String, Uri>> = MutableStateFlow(Triple("", "", Uri.EMPTY))
    private val membersFlow: MutableStateFlow<Triple<String, UserProfileEntity, Boolean>> = MutableStateFlow(Triple("", UserProfileEntity(), false))
    private val locationsFlow: MutableStateFlow<Pair<String, String>> = MutableStateFlow("" to "")

    private val _groupsFlow = MutableStateFlow<List<GroupUiModel>>(emptyList())
    override val groupsFlow: StateFlow<List<GroupUiModel>> = _groupsFlow.asStateFlow()

    override fun setGroupsCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope

        coroutineScope.launch {
            combine(avatarsFlow, locationsFlow, membersFlow, avatarsMembersFlow) { avatar, location, user, memberAvatar ->
                val currentGroups = _groupsFlow.value.toMutableList()

                fun updateGroup(groupId: String, updateAction: GroupUiModel.() -> GroupUiModel) {
                    val currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                    val index = currentGroups.indexOf(currentGroup)
                    val updatedGroup = currentGroup.updateAction()
                    if (index == -1)
                        currentGroups.add(updatedGroup)
                    else {
                        currentGroups.removeAt(index)
                        currentGroups.add(index, updatedGroup)
                    }
                }

                if (avatar.first.isNotEmpty()) {
                    updateGroup(avatar.first) {
                        copy(id = avatar.first, avatar = avatar.second)
                    }
                }

                if (location.first.isNotEmpty()) {
                    updateGroup(location.first) {
                        copy(id = location.first, address = location.second)
                    }
                }

                if (user.first.isNotEmpty()) {
                    updateGroup(user.first) {
                        val currentMembers = members.toMutableList()
                        var member = currentMembers.find { it.id == user.second.id } ?: MemberUiModel()
                        val indexOfUser = members.indexOf(member)
                        member = member.copy(
                            id = user.second.id,
                            name = user.second.name
                        )

                        if (user.third)
                            member = member.copy(
                                phone = "${user.second.countryCode}${user.second.phone}",
                                email = user.second.email
                            )

                        currentMembers.apply {
                            if (indexOfUser == -1)
                                add(member)
                            else {
                                removeAt(indexOfUser)
                                add(indexOfUser, member)
                            }
                        }

                        copy(id = user.first, members = currentMembers)
                    }
                }

                if (memberAvatar.first.isNotEmpty()) {
                    updateGroup(memberAvatar.first) {
                        val currentMembers = members.toMutableList()
                        var member = currentMembers.find { it.id == memberAvatar.second } ?: MemberUiModel()
                        val indexOfUser = members.indexOf(member)
                        member = member.copy(
                            id = memberAvatar.second,
                            avatar = memberAvatar.third
                        )

                        currentMembers.apply {
                            if (indexOfUser == -1)
                                add(member)
                            else {
                                removeAt(indexOfUser)
                                add(indexOfUser, member)
                            }
                        }

                        copy(id = memberAvatar.first, members = currentMembers)
                    }
                }

                currentGroups
            }.collect { list ->
                _groupsFlow.update {
                    list
                }
            }
        }
    }

    override fun setGroupsNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun startFetching(entities: List<GroupEntity>, isMine: Boolean) {
        val initialGroups = mutableListOf<GroupUiModel>()
        initialGroups.addAll(
            entities.map {
                GroupUiModel(
                    id = it.id,
                    adminId = it.adminId,
                    name = it.name,
                    description = it.description,
                    ages = it.ages,
                    schedule = it.schedule,
                    location = LatLng(
                        it.location.coordinates[1],
                        it.location.coordinates[0]
                    )
                )
            }
        )
        _groupsFlow.update {
            initialGroups
        }

        entities.forEach { group ->
            val members = group.members
                .groupBy { it.parentId }
                .map { (parentId, children) ->
                    MemberUiModel(id = parentId, children = children.map { it.childId }.toList())
                }

            members.forEach { member ->
                getUser(member.id, group.id, isMine)
            }

            if (group.avatar.isNotEmpty())
                getGroupAvatar(group.avatar, group.id)

            if (group.location.coordinates.isNotEmpty())
                getAddressFromLocation(
                    latLng = LatLng(
                        group.location.coordinates[1],
                        group.location.coordinates[0]
                    ),
                    group.id
                )
        }
    }

    override fun getGroups(parent: String, onSuccess: (List<GroupEntity>) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun leaveGroup(group: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.leaveGroup(group)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun deleteGroup(group: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.deleteGroup(group)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun switchAdmin(group: String, member: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.switchAdmin(group, member)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun kickUser(group: String, child: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.kickUser(group, child)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun updateGroupLocation(location: LatLng, groupId: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.updateGroupLocation(
                    groupId,
                    location.latitude,
                    location.longitude
                )
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun createGroup(
        child: String,
        name: String,
        description: String,
        onSuccess: (GroupEntity) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.createGroup(child, name, description)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun updateGroup(
        group: String,
        name: String,
        description: String,
        ages: String,
        avatar: String,
        schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
        onSuccess: () -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.updateGroup(
                    group,
                    UpdateGroupEntity(
                        name = name,
                        desc = description,
                        ages = ages,
                        schedule = schedule,
                        avatar = avatar
                    )
                )
            }
            onSuccess { onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getGroupLocation(address: String, onSuccess:(LatLng?) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                locationRepository.getLocationFromAddress(address)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getGroupAddress(
        latLng: LatLng,
        onSuccess: (String) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    private fun getUser(
        userId: String,
        groupId: String,
        isMine: Boolean
    ) {
        coroutineScope.networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                membersFlow.update {
                    Triple(groupId, user, isMine)
                }
                getUserAvatar(user.avatar, userId, groupId)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun uploadGroupAvatar(image: Bitmap, onSuccess: (String) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                filesRepository.saveAvatar(image)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    private fun getGroupAvatar(
        avatarId: String,
        groupId: String
    ) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { avatar ->
                avatarsFlow.update {
                    groupId to avatar
                }
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    private fun getUserAvatar(
        avatarId: String,
        userId: String,
        groupId: String
    ) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { avatar ->
                avatarsMembersFlow.update {
                    Triple(groupId, userId, avatar)
                }
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    private fun getAddressFromLocation(
        latLng: LatLng,
        groupId: String
    ) {
        coroutineScope.networkExecutor {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                locationsFlow.update {
                    groupId to address
                }
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }
}