package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupSearchStandaloneCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FoundGroupsStandaloneViewModel(
    private val communicator: GroupSearchStandaloneCommunicator,
    private val groupsRepository: GroupsRepository,
    private val navigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    groupsInteractor: GroupsInteractor
): ViewModel(), GroupsInteractor by groupsInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(FoundGroupViewState())
    val viewState: StateFlow<FoundGroupViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<FoundGroupUiState>(FoundGroupUiState.Idle)
    val uiState: State<FoundGroupUiState>
        get() = _uiState

    init {
        groupsInteractor.apply {
            setGroupsCoroutineScope(viewModelScope)
            setGroupsNetworkListener(this@FoundGroupsStandaloneViewModel)
        }

        findGroupsByLocation()

        _viewState.update {
            it.copy(
                avatar = Uri.parse(preferencesDatastoreManager.avatar)
            )
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.value = FoundGroupUiState.OnError(error)
    }

    fun handleEvent(event: FoundGroupEvent) {
        when (event) {
            FoundGroupEvent.ResetUiState -> _uiState.value = FoundGroupUiState.Idle
            FoundGroupEvent.OnBack -> navigator.goBack()
            FoundGroupEvent.OnJoin -> sendJoinRequest()
            is FoundGroupEvent.OnSelect -> setSelectedGroup(event.group)
            FoundGroupEvent.GoToMain ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(GROUPS_PAGE))

            FoundGroupEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))
        }
    }

    private fun sendJoinRequest() {
        networkExecutor {
            execute {
                val groupId = _viewState.value.groups.first { it.isChecked }.id
                groupsRepository.sendJoinRequest(groupId, communicator.childId)
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
                                adminId = it.adminId,
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
        getUser(
            _viewState.value.groups,
            userId,
            groupId
        ) { user, groups ->
            _viewState.update {
                it.copy(
                    groups = groups
                )
            }

            getUserAvatar(user.avatar, groupId, userId)
        }
    }

    private fun getGroupAvatar(avatarId: String, groupId: String) {
        getGroupAvatar(
            _viewState.value.groups,
            avatarId,
            groupId
        ) { groups ->
            _viewState.update {
                it.copy(
                    groups = groups
                )
            }
        }
    }

    private fun getUserAvatar(avatarId: String, groupId: String, userId: String) {
        getUserAvatar(
            _viewState.value.groups,
            avatarId,
            groupId,
            userId
        ) { groups ->
            _viewState.update {
                it.copy(
                    groups = groups
                )
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng, groupId: String) {
        getAddressFromLocation(
            _viewState.value.groups,
            latLng,
            groupId
        ) { groups ->
            _viewState.update {
                it.copy(
                    groups = groups
                )
            }
        }
    }

    companion object {

        private const val KM = 1000
    }
}