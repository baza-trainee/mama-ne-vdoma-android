package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChooseChildViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val navigator: PageNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(ChooseChildViewState())
    val viewState: StateFlow<ChooseChildViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getChildren()
    }

    fun handleEvent(event: ChooseChildEvent) {
        when (event) {
            ChooseChildEvent.ResetUiState -> _uiState.value = RequestState.Idle
            ChooseChildEvent.OnBack -> navigator.goBack()
            is ChooseChildEvent.OnChooseChild -> navigator.navigate(
                GroupsScreenRoutes.CreateGroup.getDestination(
                    event.childId
                )
            )
        }
    }

    private fun getChildren() {
        networkExecutor<List<ChildEntity>> {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                _viewState.update {
                    it.copy(
                        children = entity
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