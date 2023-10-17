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
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageWithRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SEARCH_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SETTINGS_PAGE
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
    private val groupsRepository: GroupsRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        viewModelScope.launch {
            navigator.pagesFlow.collect { page ->
                navigateToTab(page)
            }
        }

        if (page != -1)
            navigateToTab(PageWithRoute(page, CommonRoute("")))

        getUserInfo()
    }

    fun handleEvent(event: HostEvent) {
        when (event) {
            HostEvent.OnBack -> mainNavigator.goBack()
            is HostEvent.SwitchTab -> navigator.goToPage(event.index)
            HostEvent.ResetUiState -> _uiState.value = RequestState.Idle
            HostEvent.OnBackLocal -> {
                when (navigator.getCurrentRoute()) {
                    MainScreenRoutes.Main.route -> mainNavigator.goBack()
                    GroupsScreenRoutes.Groups.route,
                    SearchScreenRoutes.SearchUser.route -> navigator.goToPrevious()

                    else -> navigator.goBack()
                }
            }
        }
    }

    private fun navigateToTab(page: PageWithRoute) {
        _viewState.update {
            it.copy(currentPage = page.page)
        }
        if (page.route.destination.isEmpty()) {
            when (page.page) {
                MAIN_PAGE -> navigator.navigate(MainScreenRoutes.Main)
                GROUPS_PAGE -> navigator.navigate(GroupsScreenRoutes.Groups)
                SEARCH_PAGE -> navigator.navigate(SearchScreenRoutes.SearchUser)
                SETTINGS_PAGE -> navigator.navigate(SettingsScreenRoutes.Settings)
            }
        } else navigator.navigate(page.route)
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                if (entity.name.isEmpty() || entity.location.coordinates.isEmpty())
                    mainNavigator.navigateOnMain(viewModelScope, UserProfileRoutes.FullProfile)
                else {
                    preferencesDatastoreManager.apply {
                        id = entity.id
                        name = entity.name
                        email = entity.email
                        code = entity.countryCode
                        phone = entity.phone
                        sendEmail = entity.sendingEmails
                    }

                    getUserAvatar(entity.avatar)

                    getGroups(entity.id)

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

    private fun getGroups(parent: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess { entityList ->
                val notifications = entityList.flatMap { it.askingJoin }.size
                preferencesDatastoreManager.notifications = notifications
                _viewState.update { state ->
                    state.copy(
                        notifications = notifications
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