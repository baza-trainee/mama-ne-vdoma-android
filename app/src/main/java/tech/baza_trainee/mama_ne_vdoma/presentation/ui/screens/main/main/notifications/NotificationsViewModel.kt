package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.indexOrZero
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class NotificationsViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: PageNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(NotificationsViewState())
    val viewState: StateFlow<NotificationsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getUserInfo()
    }

    fun handleEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            NotificationsEvent.OnBack -> navigator.goToPrevious()
            is NotificationsEvent.AcceptUser -> acceptJoinRequest(event.group, event.child)
            is NotificationsEvent.DeclineUser -> declineJoinRequest(event.group, event.child)
        }
    }

    private fun acceptJoinRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.acceptRequest(group, child)
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

    private fun declineJoinRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.declineRequest(group, child)
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

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                getGroups(entity.id)

                entity.groupJoinRequests.forEach(::getGroupById)
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

    private fun getGroupById(groupId: String) {
        networkExecutor<GroupEntity> {
            execute {
                groupsRepository.getGroupById(groupId)
            }
            onSuccess { entity ->
                val currentRequests = _viewState.value.myJoinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                val members = entity.members
                    .groupBy { it.parentId }
                    .map { (parentId, children) ->
                        MemberUiModel(id = parentId, children = children.map { it.childId }.toList())
                    }
                val uiModel = GroupUiModel(
                    id = entity.id,
                    adminId = entity.adminId,
                    name = entity.name,
                    description = entity.description,
                    ages = entity.ages,
                    members = members
                )
                currentRequest = currentRequest.copy(
                    group = uiModel,
                    parentAvatar = preferencesDatastoreManager.avatarUri,
                    parentName = preferencesDatastoreManager.name,
                    parentId = preferencesDatastoreManager.id
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }

                _viewState.update {
                    it.copy(
                        myJoinRequests = currentRequests
                    )
                }

                members.forEach {
                    getUser(it.id, entity.id)
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
                val joinRequests = mutableListOf<JoinRequestUiModel>()
                entityList.onEach { group ->
                    group.askingJoin.forEach { member ->
                        getUserForJoin(member.parentId, group.id)
                        getChild(member.childId, group.id)
                        joinRequests.add(
                            JoinRequestUiModel(
                                group = GroupUiModel(
                                    id = group.id,
                                    name = group.name
                                ),
                                parentId = member.parentId
                            )
                        )
                    }
                }
                _viewState.update {
                    it.copy(
                        adminJoinRequests = joinRequests
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

    private fun getUserForJoin(userId: String, groupId: String) {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                getUserAvatarForJoin(user.avatar, groupId)

                if (user.location.coordinates.isNotEmpty())
                    getAddressFromLocation(
                        latLng = LatLng(
                            user.location.coordinates[1],
                            user.location.coordinates[0]
                        ),
                        groupId
                    )

                val currentRequests = _viewState.value.adminJoinRequests.toMutableList()

                var currentRequest =
                    currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                currentRequest = currentRequest.copy(
                    parentName = user.name,
                    parentEmail = user.email,
                    parentPhone = "${user.countryCode}${user.phone}"
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }

                _viewState.update {
                    it.copy(
                        adminJoinRequests = currentRequests
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
                getUserAvatar(user.avatar, groupId, user.id)

                val currentRequests = _viewState.value.myJoinRequests.toMutableList()

                var currentRequest =
                    currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                var group = currentRequest.group

                val currentMembers = group.members.toMutableList()
                var currentMember = currentMembers.find { it.id == userId } ?: MemberUiModel()
                val index = currentMembers.indexOrZero(currentMember)

                currentMember = currentMember.copy(name = user.name)
                currentMembers.apply {
                    if (size == 0)
                        add(currentMember)
                    else {
                        removeAt(index)
                        add(index, currentMember)
                    }
                }

                group = group.copy(members = currentMembers)

                currentRequest = currentRequest.copy(
                    group = group
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }

                _viewState.update {
                    it.copy(
                        myJoinRequests = currentRequests
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

    private fun getUserAvatar(avatarId: String, groupId: String, userId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentRequests = _viewState.value.myJoinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                var group = currentRequest.group

                val currentMembers = group.members.toMutableList()
                var currentMember = currentMembers.find { it.id == userId } ?: MemberUiModel()
                val index = currentMembers.indexOrZero(currentMember)

                currentMember = currentMember.copy(avatar = uri)
                currentMembers.apply {
                    if (size == 0)
                        add(currentMember)
                    else {
                        removeAt(index)
                        add(index, currentMember)
                    }
                }

                group = group.copy(members = currentMembers)

                currentRequest = currentRequest.copy(
                    group = group
                )
                currentRequest = currentRequest.copy(
                    parentAvatar = uri
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }
                _viewState.update {
                    it.copy(
                        myJoinRequests = currentRequests
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

    private fun getUserAvatarForJoin(avatarId: String, groupId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentRequests = _viewState.value.adminJoinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                currentRequest = currentRequest.copy(
                    parentAvatar = uri
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }
                _viewState.update {
                    it.copy(
                        adminJoinRequests = currentRequests
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

    private fun getChild(childId: String, groupId: String) {
        networkExecutor<ChildEntity> {
            execute {
                userProfileRepository.getChildById(childId)
            }
            onSuccess { child ->
                val currentRequests = _viewState.value.adminJoinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                currentRequest = currentRequest.copy(
                    child = child
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }

                _viewState.update {
                    it.copy(
                        adminJoinRequests = currentRequests
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

    private fun getAddressFromLocation(latLng: LatLng, groupId: String) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                val currentRequests = _viewState.value.adminJoinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.group.id == groupId } ?: JoinRequestUiModel()
                val indexOfRequest = currentRequests.indexOrZero(currentRequest)
                currentRequest = currentRequest.copy(
                    parentAddress = address.orEmpty()
                )
                currentRequests.apply {
                    if (size == 0)
                        add(currentRequest)
                    else {
                        removeAt(indexOfRequest)
                        add(indexOfRequest, currentRequest)
                    }
                }

                _viewState.update {
                    it.copy(
                        adminJoinRequests = currentRequests
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
}