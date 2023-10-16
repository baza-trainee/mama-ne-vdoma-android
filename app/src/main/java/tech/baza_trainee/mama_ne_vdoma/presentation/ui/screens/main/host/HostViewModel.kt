package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SEARCH_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class HostViewModel(
    private val page: Int,
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        if (page != -1)
            navigateToTab(page)

        viewModelScope.launch {
            navigator.pagesFlow.collect { page ->
                navigateToTab(page)
            }
        }

        getUserInfo()
    }

    fun handleEvent(event: HostEvent) {
        when(event) {
            HostEvent.OnBack -> mainNavigator.goBack()
            is HostEvent.SwitchTab -> navigator.goToPage(event.index)
            HostEvent.ResetUiState -> _uiState.value = RequestState.Idle
            HostEvent.OnBackLocal -> {
                when(navigator.getCurrentRoute()) {
                    MainScreenRoutes.Main.route -> mainNavigator.goBack()
                    GroupsScreenRoutes.Groups.route,
                    SearchScreenRoutes.SearchUser.route -> navigator.goToPrevious()
                    else -> navigator.goBack()
                }
            }
        }
    }

    private fun navigateToTab(page: Int) {
        _viewState.update {
            it.copy(currentPage = page)
        }
        when(page) {
            MAIN_PAGE -> navigator.navigate(MainScreenRoutes.Main)
            GROUPS_PAGE -> navigator.navigate(GroupsScreenRoutes.Groups)
            SEARCH_PAGE -> navigator.navigate(SearchScreenRoutes.SearchUser)
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

                getUserAvatar(entity.avatar)

                if (entity.location.coordinates.isNotEmpty()) {
                    getAddressFromLocation(
                        latLng = LatLng(
                            entity.location.coordinates[1],
                            entity.location.coordinates[0]
                        )
                    )
                    preferencesDatastoreManager.apply {
                        latitude = entity.location.coordinates[1]
                        longitude = entity.location.coordinates[0]
                    }
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

    private fun getUserAvatar(avatarId: String) {
        networkExecutor {
            execute { userProfileRepository.getUserAvatar(avatarId) }
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

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                preferencesDatastoreManager.address = address.orEmpty()
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