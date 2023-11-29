package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FoundGroupsStandaloneViewModel(
    private val communicator: Communicator<String>,
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
                avatar = preferencesDatastoreManager.avatarUri,
                currentUserId = preferencesDatastoreManager.id,
            )
        }

        viewModelScope.launch {
            groupsFlow.collect { groups ->
                _viewState.update {
                    it.copy(groups = groups)
                }
            }
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
                navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE))

            FoundGroupEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))

            FoundGroupEvent.CreateGroup ->
                navigator.navigate(StandaloneGroupsRoutes.ChooseChild.getDestination(isForSearch = false))
        }
    }

    private fun sendJoinRequest() {
        networkExecutor {
            execute {
                val groupId = _viewState.value.groups.find { it.isChecked }?.id.orEmpty()
                groupsRepository.sendJoinRequest(groupId, communicator.dataFlow.value)
            }
            onSuccess {
                communicator.setData("")
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
                    preferencesDatastoreManager.radius
                )
            }
            onSuccess { entityList ->
                val groups = entityList.filter { group ->
                    group.adminId != preferencesDatastoreManager.id &&
                            !group.members.map { it.parentId }.contains(preferencesDatastoreManager.id)
                }

                startFetching(groups)
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
}