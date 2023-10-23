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
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class NotificationsViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: PageNavigator
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
                        getUser(member.parentId, group.id)
                        getChild(member.childId, group.id)
                        joinRequests.add(
                            JoinRequestUiModel(
                                groupId = group.id,
                                groupName = group.name,
                                parentId = member.parentId
                            )
                        )
                    }
                }
                _viewState.update {
                    it.copy(
                        joinRequests = joinRequests
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
                getUserAvatar(user.avatar, groupId)

                if (user.location.coordinates.isNotEmpty())
                    getAddressFromLocation(
                        latLng = LatLng(
                            user.location.coordinates[1],
                            user.location.coordinates[0]
                        ),
                        groupId
                    )

                val currentRequests = _viewState.value.joinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.groupId == groupId } ?: JoinRequestUiModel()
                val indexOfGroup = currentRequests.indexOf(currentRequest)
                currentRequest = currentRequest.copy(
                    parentName = user.name,
                    parentEmail = user.email,
                    parentPhone = "${user.countryCode}${user.phone}"
                )
                currentRequests[indexOfGroup] = currentRequest

                _viewState.update {
                    it.copy(
                        joinRequests = currentRequests
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

    private fun getUserAvatar(avatarId: String, groupId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentRequests = _viewState.value.joinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.groupId == groupId } ?: JoinRequestUiModel()
                val indexOfGroup = currentRequests.indexOf(currentRequest)
                currentRequest = currentRequest.copy(
                    parentAvatar = uri
                )
                currentRequests[indexOfGroup] = currentRequest
                _viewState.update {
                    it.copy(
                        joinRequests = currentRequests
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
                val currentRequests = _viewState.value.joinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.groupId == groupId } ?: JoinRequestUiModel()
                val indexOfGroup = currentRequests.indexOf(currentRequest)
                currentRequest = currentRequest.copy(
                    child = child
                )
                currentRequests[indexOfGroup] = currentRequest
                _viewState.update {
                    it.copy(
                        joinRequests = currentRequests
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
                val currentRequests = _viewState.value.joinRequests.toMutableList()
                var currentRequest = currentRequests.find { it.groupId == groupId } ?: JoinRequestUiModel()
                val indexOfGroup = currentRequests.indexOf(currentRequest)
                currentRequest = currentRequest.copy(
                    parentAddress = address.orEmpty()
                )
                currentRequests[indexOfGroup] = currentRequest
                _viewState.update {
                    it.copy(
                        joinRequests = currentRequests
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