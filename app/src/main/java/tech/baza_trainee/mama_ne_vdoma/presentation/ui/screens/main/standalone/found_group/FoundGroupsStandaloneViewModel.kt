package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import android.net.Uri
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
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupSearchStandaloneCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FoundGroupsStandaloneViewModel(
    private val communicator: GroupSearchStandaloneCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(FoundGroupViewState())
    val viewState: StateFlow<FoundGroupViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<FoundGroupUiState>(FoundGroupUiState.Idle)
    val uiState: State<FoundGroupUiState>
        get() = _uiState

    init {
        findGroupsByLocation()
        _viewState.update {
            it.copy(
                avatar = Uri.parse(preferencesDatastoreManager.avatar)
            )
        }
    }

    fun handleEvent(event: FoundGroupEvent) {
        when (event) {
            FoundGroupEvent.ResetUiState -> _uiState.value = FoundGroupUiState.Idle
            FoundGroupEvent.OnBack -> navigator.goBack()
            FoundGroupEvent.OnJoin -> sendJoinRequest()
            is FoundGroupEvent.OnSelect -> setSelectedGroup(event.group)
            FoundGroupEvent.GoToMain ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(GROUPS_PAGE))
        }
    }

    private fun sendJoinRequest() {
        networkExecutor {
            execute {
                val groupId = _viewState.value.groups.first { it.isChecked }.id
                groupsRepository.joinToGroup(groupId, communicator.childId)
            }
            onSuccess {
                _uiState.value = FoundGroupUiState.OnRequestSent
            }
            onError { error ->
                _uiState.value = FoundGroupUiState.OnError(error)
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

    private fun setSelectedGroup(groupId: String) {
        _viewState.update { state ->
            val groups = mutableListOf<GroupUiModel>()
            state.groups.forEach {
                val group = it.copy(isChecked = it.id == groupId)
                groups.add(group)
            }
            state.copy(
                groups = groups
            )
        }
    }

    private fun findGroupsByLocation() {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsByArea(
                    preferencesDatastoreManager.latitude,
                    preferencesDatastoreManager.longitude,
                    KM * preferencesDatastoreManager.radius
                )
            }
            onSuccess { entityList ->
                _viewState.update { state ->
                    state.copy(
                        groups = entityList.map {
                            GroupUiModel(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                ages = it.ages
                            )
                        }
                    )
                }
                entityList.forEach { group ->
                    group.members.forEach { member ->
                        getUser(member.parentId, group.id)
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
                _uiState.value = FoundGroupUiState.OnError(error)
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
                getUserAvatar(user.avatar, groupId, userId)

                val currentGroups = _viewState.value.groups.toMutableList()
                var currentGroup = currentGroups.find { it.id == groupId }
                val index = currentGroups.indexOf(currentGroup)
                val currentMembers = currentGroup?.members?.toMutableList()
                val member = MemberUiModel(
                    id = user.id,
                    name = user.name
                )
                currentMembers?.add(member)
                currentGroup = currentGroup?.copy(members = currentMembers.orEmpty())
                currentGroups[index] = currentGroup ?: GroupUiModel()

                _viewState.update {
                    it.copy(
                        groups = currentGroups
                    )
                }
            }
            onError { error ->
                _uiState.value = FoundGroupUiState.OnError(error)
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
                var currentGroup = currentGroups.find { it.id == groupId } ?: GroupUiModel()
                val indexOfGroup = currentGroups.indexOf(currentGroup)
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
            onError { error ->
                _uiState.value = FoundGroupUiState.OnError(error)
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
                var currentGroup = currentGroups.find { it.id == groupId }
                val indexOfGroup = currentGroups.indexOf(currentGroup)
                val currentMembers = currentGroup?.members?.toMutableList()
                var currentUser = currentGroup?.members?.find { it.id == userId } ?: MemberUiModel()
                val indexOfUser = currentGroup?.members?.indexOf(currentUser) ?: 0
                currentUser = currentUser.copy(
                    avatar = uri
                )
                currentMembers?.apply{
                    removeAt(indexOfUser)
                    add(indexOfUser, currentUser)
                }
                currentGroup = currentGroup?.copy(members = currentMembers.orEmpty())
                currentGroups[indexOfGroup] = currentGroup ?: GroupUiModel()

                _viewState.update {
                    it.copy(
                        groups = currentGroups
                    )
                }
            }
            onError { error ->
                _uiState.value = FoundGroupUiState.OnError(error)
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
                var currentGroup = currentGroups.find { it.id == groupId }
                val indexOfGroup = currentGroups.indexOf(currentGroup)
                currentGroup = currentGroup?.copy(
                    location = address.orEmpty()
                )
                currentGroups[indexOfGroup] = currentGroup ?: GroupUiModel()
                _viewState.update {
                    it.copy(
                        groups = currentGroups
                    )
                }
            }
            onError { error ->
                _uiState.value = FoundGroupUiState.OnError(error)
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

    companion object {

        private const val KM = 1000
    }
}