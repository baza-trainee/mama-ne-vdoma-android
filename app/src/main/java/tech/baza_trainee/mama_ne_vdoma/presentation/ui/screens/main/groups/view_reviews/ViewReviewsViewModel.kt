package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.UserReviewUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class ViewReviewsViewModel(
    private val userId: String,
    private val navigator: PageNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(ViewReviewsViewState())
    val viewState: StateFlow<ViewReviewsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getUserRating()
    }

    fun handleEvent(event: ViewReviewsEvent) {
        when(event) {
            ViewReviewsEvent.OnBack -> navigator.goToPrevious()
            ViewReviewsEvent.ResetUiState -> _uiState.value = RequestState.Idle
        }
    }

    private fun getUserRating() {
        networkExecutor<List<UserRatingDomainModel>> {
            execute {
                userProfileRepository.getUserGrade(userId)
            }
            onSuccess { reviews ->
                viewModelScope.launch {
                    val reviewList = reviews.map { review ->
                        async {
                            getMember(review)
                        }
                    }
                    _viewState.update {
                        it.copy(reviews = reviewList.awaitAll())
                    }
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private suspend fun getMember(review: UserRatingDomainModel): UserReviewUiModel {
        setProgress(true)

        val result1 = userProfileRepository.getUserById(review.reviewer)
        val member = getResult(result1) ?: UserProfileEntity()

        val result2 = filesRepository.getAvatar(member.avatar)
        val avatar = getResult(result2) ?: Uri.EMPTY

        setProgress(false)

        val timestamp = review.timestamp.takeIf { it.isNotEmpty() }
            ?.let { Instant.parse(it) }
            ?.let { Date.from(it) }
            ?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) }
            .orEmpty()

        return UserReviewUiModel(
            avatar = avatar,
            rating = review.rating.toInt(),
            note = review.message,
            name = member.name,
            timestamp = timestamp
        )
    }

    private fun setProgress(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun <T> getResult(result: RequestResult<T>): T? {
        return when(result) {
            is RequestResult.Error -> {
                _uiState.value = RequestState.OnError(result.error)
                null
            }

            is RequestResult.Success -> result.result
        }
    }
}
