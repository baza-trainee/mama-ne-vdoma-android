package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class RateUserViewModel(
    private val userId: String,
    private val navigator: PageNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(RateUserViewState())
    val viewState: StateFlow<RateUserViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RateUserUiState>(RateUserUiState.Idle)
    val uiState: StateFlow<RateUserUiState>
        get() = _uiState.asStateFlow()

    init {
        getUser()
        getUserRating()
    }

    fun handleEvent(event: RateUserEvent) {
        when(event) {
            RateUserEvent.OnBack -> navigator.goToPrevious()
            RateUserEvent.OnSave -> setUserRating()
            RateUserEvent.ResetUiState -> _uiState.update { RateUserUiState.Idle }
            is RateUserEvent.SetNote ->
                _viewState.update {
                    it.copy(note = event.value)
                }
            is RateUserEvent.SetRating ->
                _viewState.update {
                    it.copy(currentRating = event.value)
                }

            RateUserEvent.ViewReviews ->
                navigator.navigate(GroupsScreenRoutes.ViewReviews.getDestination(userId))

            RateUserEvent.GoToMain -> navigator.navigate(MainScreenRoutes.Main)
        }
    }

    private fun getUser() {
        networkExecutor<UserProfileEntity> {
            execute { userProfileRepository.getUserById(userId) }
            onSuccess { user ->
                _viewState.update {
                    it.copy(
                        name = user.name,
                        rating = user.rating
                    )
                }

                getUserAvatar(user.avatar)
            }
            onError { error ->
                _uiState.update { RateUserUiState.OnError(error) }
            }
            onLoading(::setProgress)
        }
    }

    private fun getUserAvatar(avatarId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                _viewState.update {
                    it.copy(avatar = uri)
                }
            }
            onError { error ->
                _uiState.update { RateUserUiState.OnError(error) }
            }
            onLoading(::setProgress)
        }
    }

    private fun setUserRating() {
        networkExecutor {
            execute {
                userProfileRepository.setUserGrade(
                    userId,
                    UserRatingDomainModel(
                        _viewState.value.currentRating.toFloat(),
                        _viewState.value.note
                    )
                )
            }
            onSuccess { _uiState.update { RateUserUiState.OnSet } }
            onError { error ->
                _uiState.update { RateUserUiState.OnError(error) }
            }
            onLoading(::setProgress)
        }
    }

    private fun getUserRating() {
        networkExecutor<List<UserRatingDomainModel>> {
            execute {
                userProfileRepository.getUserGrade(userId)
            }
            onSuccess { reviews ->
                _viewState.update {
                    it.copy(reviews = reviews.size)
                }
            }
            onError { error ->
                _uiState.update { RateUserUiState.OnError(error) }
            }
            onLoading(::setProgress)
        }
    }

    private fun setProgress(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }
}
