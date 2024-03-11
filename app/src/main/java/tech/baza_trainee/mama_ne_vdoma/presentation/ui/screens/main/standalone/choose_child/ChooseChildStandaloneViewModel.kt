package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NOTIFICATIONS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChooseChildStandaloneViewModel(
    private val isForSearch: Boolean,
    private val communicator: Communicator<String>,
    private val userProfileRepository: UserProfileRepository,
    private val navigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(ChooseChildViewState())
    val viewState: StateFlow<ChooseChildViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesDatastoreManager.userPreferencesFlow.collect { pref ->
                _viewState.update {
                    it.copy(
                        avatar = pref.avatarUri,
                        notifications = pref.myJoinRequests + pref.adminJoinRequests
                    )
                }
            }
        }

        getChildren()
    }

    fun handleEvent(event: ChooseChildEvent) {
        when (event) {
            ChooseChildEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            ChooseChildEvent.OnBack -> navigator.goBack()
            is ChooseChildEvent.OnChooseChild -> {
                communicator.setData(event.childId)
                if (isForSearch)
                    navigator.navigate(StandaloneGroupsRoutes.SetArea)
                else
                    navigator.navigate(StandaloneGroupsRoutes.CreateGroup)
            }

            ChooseChildEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))

            ChooseChildEvent.GoToNotifications ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(NOTIFICATIONS_PAGE))
        }
    }


    private fun getChildren() {
        networkExecutor {
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