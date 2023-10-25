package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.indexOrZero
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

interface GroupsInteractor {

    fun setGroupsCoroutineScope(coroutineScope: CoroutineScope)

    fun setGroupsNetworkListener(listener: NetworkEventsListener)

    fun getUser(groups: List<GroupUiModel>, userId: String, groupId: String, onSuccess: (UserProfileEntity, List<GroupUiModel>) -> Unit)

    fun getGroupAvatar(groups: List<GroupUiModel>, avatarId: String, groupId: String, onSuccess: (List<GroupUiModel>) -> Unit)

    fun getUserAvatar(groups: List<GroupUiModel>, avatarId: String, groupId: String, userId: String, onSuccess: (List<GroupUiModel>) -> Unit)

    fun getAddressFromLocation(groups: List<GroupUiModel>, latLng: LatLng, groupId: String, onSuccess: (List<GroupUiModel>) -> Unit)
}

class GroupsInteractorImpl(
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val filesRepository: FilesRepository
) : GroupsInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener

    override fun setGroupsCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun setGroupsNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun getUser(
        groups: List<GroupUiModel>,
        userId: String,
        groupId: String,
        onSuccess: (UserProfileEntity, List<GroupUiModel>) -> Unit
    ) {
        coroutineScope.networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                val currentGroups = groups.toMutableList()
                var currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                val index = currentGroups.indexOrZero(currentGroup)
                val currentMembers = currentGroup.members.toMutableList()
                var member = currentMembers.find { it.id == userId } ?: MemberUiModel()
                val indexOfUser = currentGroup.members.indexOrZero(member)
                member = member.copy(
                    name = user.name,
                    email = user.email,
                    phone = "${user.countryCode}${user.phone}"
                )
                currentMembers.apply {
                    if (size == 0)
                        add(member)
                    else {
                        removeAt(indexOfUser)
                        add(indexOfUser, member)
                    }
                }
                currentGroup = currentGroup.copy(members = currentMembers)
                currentGroups[index] = currentGroup

                onSuccess(user, currentGroups)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getGroupAvatar(
        groups: List<GroupUiModel>,
        avatarId: String,
        groupId: String,
        onSuccess: (List<GroupUiModel>) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentGroups = groups.toMutableList()
                var currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOrZero(currentGroup)
                currentGroup = currentGroup.copy(
                    avatar = uri
                )
                currentGroups[indexOfGroup] = currentGroup

                onSuccess(currentGroups)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getUserAvatar(
        groups: List<GroupUiModel>,
        avatarId: String,
        groupId: String,
        userId: String,
        onSuccess: (List<GroupUiModel>) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentGroups = groups.toMutableList()
                var currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOrZero(currentGroup)
                val currentMembers = currentGroup.members.toMutableList()
                var member =
                    currentGroup.members.find { it.id == userId } ?: MemberUiModel()
                val indexOfUser = currentGroup.members.indexOrZero(member)
                member = member.copy(
                    avatar = uri
                )
                currentMembers.apply {
                    if (size == 0)
                        add(member)
                    else {
                        removeAt(indexOfUser)
                        add(indexOfUser, member)
                    }
                }

                currentGroup = currentGroup.copy(members = currentMembers)
                currentGroups.apply {
                    if (size == 0)
                        add(currentGroup)
                    else {
                        removeAt(indexOfGroup)
                        add(indexOfGroup, currentGroup)
                    }
                }

                onSuccess(currentGroups)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getAddressFromLocation(
        groups: List<GroupUiModel>,
        latLng: LatLng,
        groupId: String,
        onSuccess: (List<GroupUiModel>) -> Unit
    ) {
        coroutineScope.networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                val currentGroups = groups.toMutableList()
                var currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOrZero(currentGroup)
                currentGroup = currentGroup.copy(
                    location = address.orEmpty()
                )
                currentGroups.apply {
                    if (size == 0)
                        add(currentGroup)
                    else {
                        removeAt(indexOfGroup)
                        add(indexOfGroup, currentGroup)
                    }
                }

                onSuccess(currentGroups)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }
}