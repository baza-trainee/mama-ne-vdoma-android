package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class MyGroupsViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: PageNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(MyGroupsViewState())
    val viewState: StateFlow<MyGroupsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getUserInfo()
    }

    fun handleEvent(event: MyGroupsEvent) {
        when (event) {
            MyGroupsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            MyGroupsEvent.OnBack -> navigator.goToPrevious()
            MyGroupsEvent.CreateNewGroup -> navigator.navigate(GroupsScreenRoutes.ChooseChild)
            is MyGroupsEvent.OnKick -> event.children.forEach { kickUser(event.group, it) }
            is MyGroupsEvent.OnLeave -> leaveGroup(event.group)
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                getGroups(entity.id)
                _viewState.update {
                    it.copy(
                        userId = entity.id
                    )
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getGroups(parent: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess { entityList ->
                entityList.forEach { group ->
                    val members = group.members
                        .groupBy { it.parentId }
                        .map { (parentId, children) ->
                            MemberUiModel(id = parentId, children = children.map { it.childId }.toList())
                        }

                    _viewState.update { state ->
                        state.copy(
                            groups = entityList.map {
                                GroupUiModel(
                                    id = it.id,
                                    adminId = it.adminId,
                                    name = it.name,
                                    description = it.description,
                                    ages = it.ages,
                                    members = members
                                )
                            }
                        )
                    }

                    members.forEach { member ->
                        getUser(member.id, group.id)
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
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUser(userId: String, groupId: String) {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                val currentGroups = _viewState.value.groups.toMutableList()
                var currentGroup = currentGroups.firstOrNull { it.id == groupId } ?: GroupUiModel()
                val index = currentGroups.indexOf(currentGroup)
                if (index != -1) {
                    val currentMembers = currentGroup.members.toMutableList()
                    var member = currentMembers.firstOrNull { it.id == userId } ?: MemberUiModel()
                    val indexOfUser = currentGroup.members.indexOf(member)
                    if (indexOfUser != -1) {
                        member = member.copy(
                            name = user.name,
                            email = user.email,
                            phone = "${user.countryCode}${user.phone}"
                        )
                        currentMembers.apply {
                            removeAt(indexOfUser)
                            add(indexOfUser, member)
                        }
                        currentGroup = currentGroup.copy(members = currentMembers)
                        currentGroups[index] = currentGroup

                        _viewState.update {
                            it.copy(
                                groups = currentGroups
                            )
                        }
                    }
                }

                getUserAvatar(user.avatar, groupId, userId)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getGroupAvatar(avatarId: String, groupId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentGroups = _viewState.value.groups.toMutableList()
                var currentGroup = currentGroups.firstOrNull { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOf(currentGroup)
                if (indexOfGroup != -1) {
                    currentGroup = currentGroup.copy(
                        avatar = uri
                    )
                    currentGroups[indexOfGroup] = currentGroup

                    _viewState.update {
                        it.copy(
                            groups = currentGroups
                        )
                    }
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserAvatar(avatarId: String, groupId: String, userId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentGroups = _viewState.value.groups.toMutableList()
                var currentGroup = currentGroups.firstOrNull { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOf(currentGroup)
                if (indexOfGroup != -1) {
                    val currentMembers = currentGroup.members.toMutableList()
                    var member =
                        currentGroup.members.firstOrNull() { it.id == userId } ?: MemberUiModel()
                    val indexOfUser = currentGroup.members.indexOf(member)
                    if (indexOfUser != -1) {
                        member = member.copy(
                            avatar = uri
                        )
                        currentMembers.apply {
                            removeAt(indexOfUser)
                            add(indexOfUser, member)
                        }
                        currentGroup = currentGroup.copy(members = currentMembers)
                        currentGroups[indexOfGroup] = currentGroup

                        _viewState.update {
                            it.copy(
                                groups = currentGroups
                            )
                        }
                    }
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng, groupId: String) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                val currentGroups = _viewState.value.groups.toMutableList()
                var currentGroup = currentGroups.firstOrNull { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOf(currentGroup)
                if (indexOfGroup != -1) {
                    currentGroup = currentGroup.copy(
                        location = address.orEmpty()
                    )
                    currentGroups[indexOfGroup] = currentGroup
                    _viewState.update {
                        it.copy(
                            groups = currentGroups
                        )
                    }
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun leaveGroup(groupId: String) {
        networkExecutor {
            execute {
                groupsRepository.leaveGroup(groupId)
            }
            onSuccess { getUserInfo() }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun kickUser(groupId: String, childId: String) {
        networkExecutor {
            execute {
                groupsRepository.kickUser(groupId, childId)
            }
            onSuccess { getUserInfo() }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}