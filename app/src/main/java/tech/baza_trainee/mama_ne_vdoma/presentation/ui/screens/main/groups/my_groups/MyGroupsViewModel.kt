package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class MyGroupsViewModel(
    private val userAuthRepository: UserAuthRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val navigator: PageNavigator,
    private val mainNavigator: ScreenNavigator,
    private val groupsInteractor: GroupsInteractor,
    private val communicator: Communicator<GroupUiModel>
): ViewModel(), GroupsInteractor by groupsInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(MyGroupsViewState())
    val viewState: StateFlow<MyGroupsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        groupsInteractor.apply {
            setGroupsCoroutineScope(viewModelScope)
            setGroupsNetworkListener(this@MyGroupsViewModel)
        }

        getUserInfo()

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
        _uiState.value = RequestState.OnError(error)
    }

    fun handleEvent(event: MyGroupsEvent) {
        when (event) {
            MyGroupsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            MyGroupsEvent.OnBack -> navigator.goToPrevious()
            MyGroupsEvent.CreateNewGroup ->
                mainNavigator.navigate(
                    StandaloneGroupsRoutes.ChooseChild.getDestination(isForSearch = false)
                )
            is MyGroupsEvent.OnLeave -> leaveGroup(event.group)
            is MyGroupsEvent.OnDelete -> deleteGroup(event.group)
            is MyGroupsEvent.OnSwitchAdmin -> switchAdmin(event.group, event.member)
            is MyGroupsEvent.OnEdit -> {
                val group = _viewState.value.groups.find { it.id == event.group } ?: GroupUiModel()
                communicator.setData(group)
                navigator.navigate(GroupsScreenRoutes.UpdateGroup)
            }

            is MyGroupsEvent.OnRateUser ->
                navigator.navigate(GroupsScreenRoutes.RateUser.getDestination(event.user))
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userAuthRepository.getUserInfo()
            }
            onSuccess { entity ->
                getGroups(entity.id)
                _viewState.update {
                    it.copy(
                        userId = entity.id
                    )
                }

                preferencesDatastoreManager.myJoinRequests = entity.groupJoinRequests.size
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
        getGroups(parent) { entities ->
            startFetching(entities, true)
            preferencesDatastoreManager.adminJoinRequests = entities.flatMap { it.askingJoin }.size
        }
    }

    private fun leaveGroup(groupId: String) {
        leaveGroup(groupId) { getUserInfo() }
    }

    private fun deleteGroup(groupId: String) {
        deleteGroup(groupId) { getUserInfo() }
    }

    private fun switchAdmin(groupId: String, memberId: String) {
        switchAdmin(groupId, memberId) { getUserInfo() }
    }
}