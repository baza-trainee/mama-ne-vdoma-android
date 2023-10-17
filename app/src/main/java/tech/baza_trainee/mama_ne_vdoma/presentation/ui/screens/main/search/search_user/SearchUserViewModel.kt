package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

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
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SearchResultsCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInSearchUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class SearchUserViewModel(
    private val navigator: PageNavigator,
    private val communicator: SearchResultsCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(SearchUserViewState())
    val viewState: StateFlow<SearchUserViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<SearchUserUiState>(SearchUserUiState.Idle)
    val uiState: State<SearchUserUiState>
        get() = _uiState

    fun handleEvent(event: SearchUserEvent) {
        when(event) {
            SearchUserEvent.OnBack -> navigator.goToPrevious()
            SearchUserEvent.ResetUiState -> _uiState.value = SearchUserUiState.Idle
            SearchUserEvent.OnSearch -> searchUser()
            is SearchUserEvent.ValidateEmail -> validateEmail(event.value)
            is SearchUserEvent.ValidateName -> validateName(event.value)
        }
    }

    private fun validateName(name: String) {
        val nameValid = if (name.length in NAME_LENGTH &&
            name.all { it.isLetter() || it.isDigit() || it == ' ' || it == '-' })
            ValidField.VALID
        else
            ValidField.INVALID

        _viewState.update {
            it.copy(
                name =  name,
                nameValid = nameValid
            )
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
            onError { error ->
                _uiState.value = SearchUserUiState.OnError(error)
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

        private val NAME_LENGTH = 2..18
    }
}