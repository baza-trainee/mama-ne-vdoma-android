package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SearchResultsCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupsInSearchUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class SearchResultsViewModel(
    private val navigator: PageNavigator,
    private val communicator: SearchResultsCommunicator,
    private val groupsRepository: GroupsRepository,
    private val filesRepository: FilesRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(SearchResultsViewState())
    val viewState: StateFlow<SearchResultsViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    init {
        getGroups(communicator.user.id)
        getUserAvatar(communicator.avatarId)
        _viewState.update {
            it.copy(
                parent = communicator.user
            )
        }
    }

    fun handleEvent(event: SearchResultsEvent) {
        when(event) {
            SearchResultsEvent.OnBack -> navigator.goToPrevious()
            SearchResultsEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            SearchResultsEvent.OnNewSearch -> navigator.goToPrevious()
        }
    }

    private fun getGroups(parent: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess { entityList ->
                val groups = entityList.map { GroupsInSearchUiModel(it.name, it.id) }.toList()
                val currentParent = _viewState.value.parent.copy(
                    groups = groups
                )
                _viewState.update {
                    it.copy(parent = currentParent)
                }
            }
            onError { error ->
                _uiState.update { RequestState.OnError(error) }
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }

    private fun getUserAvatar(avatarId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                val currentParent = _viewState.value.parent.copy(avatar = uri)

                _viewState.update {
                    it.copy(parent = currentParent)
                }
            }
            onError { error ->
                _uiState.update { RequestState.OnError(error) }
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }
}