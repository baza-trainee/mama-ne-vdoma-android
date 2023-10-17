package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.InitialGroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.ChooseChildViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupSearchStandaloneCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChooseChildStandaloneViewModel(
    private val communicator: GroupSearchStandaloneCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val navigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(ChooseChildViewState())
    val viewState: StateFlow<ChooseChildViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getUserInfo()
        getChildren()
    }

    fun handleEvent(event: ChooseChildEvent) {
        when (event) {
            ChooseChildEvent.ResetUiState -> _uiState.value = RequestState.Idle
            ChooseChildEvent.OnBack -> navigator.goBack()
            is ChooseChildEvent.OnChooseChild -> {
                communicator.childId = event.childId
                navigator.navigate(
                    InitialGroupSearchRoutes.SetArea
                )
            }
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                preferencesDatastoreManager.apply {
                    id = entity.id
                    name = entity.name
                    email = entity.email
                }

                if (entity.location.coordinates.isNotEmpty()) {
                    preferencesDatastoreManager.apply {
                        latitude = entity.location.coordinates[1]
                        longitude = entity.location.coordinates[0]
                    }
                }

                getUserAvatar(entity.avatar)
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

    private fun getUserAvatar(avatarId: String) {
        networkExecutor<Uri> {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                preferencesDatastoreManager.avatar = uri.toString()
                _viewState.update {
                    it.copy(avatar = uri)
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