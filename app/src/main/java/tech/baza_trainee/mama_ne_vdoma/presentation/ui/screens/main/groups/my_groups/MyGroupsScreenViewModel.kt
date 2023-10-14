package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.PREV_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class MyGroupsScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userProfileRepository: UserProfileRepository,
    private val groupsRepository: GroupsRepository,
    private val navigator: ScreenNavigator
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
            MyGroupsEvent.OnBack -> {
                val prevPage = savedStateHandle.get<Int>(PREV_PAGE) ?: MAIN_PAGE
                when(prevPage) {
                    MAIN_PAGE -> navigator.navigate(MainScreenRoutes.Main)
                }
                savedStateHandle[PAGE] = prevPage
            }
            MyGroupsEvent.CreateNewGroup -> navigator.navigate(GroupsScreenRoutes.ChooseChild)
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity?> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                entity?.id?.let {
                    getGroups(it)
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
            onSuccess { entity ->
                _viewState.update {
                    it.copy(
                        groups = entity
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