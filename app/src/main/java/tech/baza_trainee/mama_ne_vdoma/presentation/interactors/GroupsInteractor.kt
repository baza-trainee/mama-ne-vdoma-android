package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

interface GroupsInteractor {

    val groupsFlow: StateFlow<List<GroupUiModel>>

    fun setGroupsCoroutineScope(coroutineScope: CoroutineScope)

    fun setGroupsNetworkListener(listener: NetworkEventsListener)

    fun startFetching(entities: List<GroupEntity>, isMine: Boolean = false)
}

class GroupsInteractorImpl(
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

    private val initialGroups = mutableListOf<GroupUiModel>()

    override fun setGroupsCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope

        coroutineScope.launch {
            combine(avatarsFlow, locationsFlow, membersFlow, avatarsMembersFlow) { avatar, location, user, memberAvatar ->
                if (avatar.first.isNotEmpty()) {
                    var currentGroup =
                        initialGroups.find { it.id == avatar.first } ?: GroupUiModel()
                    val index = initialGroups.indexOf(currentGroup)
                    currentGroup = currentGroup.copy(id = avatar.first, avatar = avatar.second)
                    if (index == -1)
                        initialGroups.add(currentGroup)
                    else {
                        initialGroups.removeAt(index)
                        initialGroups.add(index, currentGroup)
                    }
                }

                if (location.first.isNotEmpty()) {
                    var currentGroup = initialGroups.find { it.id == location.first } ?: GroupUiModel()
                    val index = initialGroups.indexOf(currentGroup)
                    currentGroup = currentGroup.copy(location = location.second)
                    if (index == -1)
                        initialGroups.add(currentGroup)
                    else {
                        initialGroups.removeAt(index)
                        initialGroups.add(index, currentGroup)
                    }
                }

                if (user.first.isNotEmpty()) {
                    var currentGroup = initialGroups.find { it.id == user.first } ?: GroupUiModel()
                    val index = initialGroups.indexOf(currentGroup)
                    val currentMembers = currentGroup.members.toMutableList()
                    var member = currentMembers.find { it.id == user.second.id } ?: MemberUiModel()
                    val indexOfUser = currentGroup.members.indexOf(member)
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
                    currentGroup = currentGroup.copy(id = user.first, members = currentMembers)
                    if (index == -1)
                        initialGroups.add(currentGroup)
                    else {
                        initialGroups.removeAt(index)
                        initialGroups.add(index, currentGroup)
                    }
                }

                if (memberAvatar.first.isNotEmpty()) {
                    var currentGroup =
                        initialGroups.find { it.id == memberAvatar.first } ?: GroupUiModel()
                    val index = initialGroups.indexOf(currentGroup)
                    val currentMembers = currentGroup.members.toMutableList()
                    var member = currentMembers.find { it.id == memberAvatar.second } ?: MemberUiModel()
                    val indexOfUser = currentGroup.members.indexOf(member)
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
                    currentGroup =
                        currentGroup.copy(id = memberAvatar.first, members = currentMembers)
                    if (index == -1)
                        initialGroups.add(currentGroup)
                    else {
                        initialGroups.removeAt(index)
                        initialGroups.add(index, currentGroup)
                    }
                }

                initialGroups
            }.collect {
                _groupsFlow.update {
                    initialGroups
                }
            }
        }
    }

    override fun setGroupsNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun startFetching(entities: List<GroupEntity>, isMine: Boolean) {
        initialGroups.addAll(
            entities.map {
                GroupUiModel(
                    id = it.id,
                    adminId = it.adminId,
                    name = it.name,
                    description = it.description,
                    ages = it.ages
                )
            }
        )

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