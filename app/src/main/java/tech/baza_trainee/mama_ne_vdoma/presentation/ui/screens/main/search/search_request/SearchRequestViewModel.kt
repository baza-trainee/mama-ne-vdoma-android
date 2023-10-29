package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SearchResultsCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInSearchUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onErrorWithCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.net.HttpURLConnection

class SearchRequestViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val communicator: SearchResultsCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(SearchRequestViewState())
    val viewState: StateFlow<SearchRequestViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<SearchRequestUiState>(SearchRequestUiState.Idle)
    val uiState: State<SearchRequestUiState>
        get() = _uiState

    fun handleEvent(event: SearchRequestEvent) {
        when(event) {
            SearchRequestEvent.OnBack -> navigator.goToPrevious()
            SearchRequestEvent.ResetUiState -> _uiState.value = SearchRequestUiState.Idle
            SearchRequestEvent.SearchUser -> searchUser()
            is SearchRequestEvent.ValidateEmail -> validateEmail(event.value)
            SearchRequestEvent.OnMain -> navigator.navigate(MainScreenRoutes.Main)
            SearchRequestEvent.SearchGroup -> mainNavigator.navigate(StandaloneGroupsRoutes.ChooseChild.getDestination(isForSearch = true))
        }
    }

    private fun validateEmail(email: String) {
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                email = email,
                emailValid = emailValid
            )
        }
    }

    private fun searchUser() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserByEmail(_viewState.value.email)
            }
            onSuccess {
                val parent = ParentInSearchUiModel(
                    id = it.id,
                    name = it.name,
                    email = it.email
                )
                communicator.apply {
                    user = parent
                    avatarId = it.avatar
                }
                navigator.navigate(SearchScreenRoutes.SearchResults)
            }
            onErrorWithCode { error, code ->
                if (code == HttpURLConnection.HTTP_NOT_FOUND)
                    _uiState.value = SearchRequestUiState.OnNothingFound
                else
                    _uiState.value = SearchRequestUiState.OnError(error)
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