package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.ChatScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonHostRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.CHAT_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.EDIT_PROFILE_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NOTIFICATIONS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SEARCH_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onErrorWithCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.net.HttpURLConnection
import java.time.DayOfWeek

class HostViewModel(
    page: Int,
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val locationRepository: LocationRepository,
    private val groupsRepository: GroupsRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val oneTapClient: SignInClient
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    private var goToPage = page

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

        viewModelScope.launch {
            navigator.routesFlow.collect {
                if (it.page != -1) switchTab(it)
            }
        }

        if (goToPage != -1) {
            val route = getRouteFromPage(goToPage)
            switchTab(route)
            navigator.navigate(route)
            goToPage = -1
        }

        getUserInfo()
    }

    fun handleEvent(event: HostEvent) {
        when (event) {
            HostEvent.OnBack -> mainNavigator.minimize()
            is HostEvent.SwitchTab -> {
                val route = getRouteFromPage(event.index)
                switchTab(route)
                navigator.navigate(route)
            }
            HostEvent.ResetUiState -> _uiState.value = RequestState.Idle
            HostEvent.OnBackLocal -> {
                when (navigator.getCurrentRoute()) {
                    MainScreenRoutes.Main.route -> mainNavigator.minimize()
                    else -> navigator.goToPrevious()
                }
            }

            HostEvent.GoToNotifications -> navigator.navigate(MainScreenRoutes.Notifications)
            HostEvent.OnCheckSession -> checkSession()
        }
    }

    private fun checkSession() {
        viewModelScope.networkExecutor {
            execute {
                userAuthRepository.getUserInfo()
            }
            onErrorWithCode { _, code ->
                if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    oneTapClient.signOut()
                    mainNavigator.navigate(LoginRoutes.Login)
                }
            }
        }
    }

    private fun switchTab(route: CommonHostRoute) {
        _viewState.update {
            it.copy(
                currentRoute = route
            )
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userAuthRepository.getUserInfo()
            }
            onSuccess { entity ->
                val _isUserInfoFilled = entity.name.isNotEmpty() &&
                        entity.phone.isNotEmpty() &&
                        entity.location.coordinates.isNotEmpty() && entity.location.coordinates.none { it == 0.00 } &&
                        entity.schedule.values.any { it.isFilled() }

                if (!_isUserInfoFilled)
                    navigator.navigate(SettingsScreenRoutes.EditProfile)
                else {
                    preferencesDatastoreManager.apply {
                        id = entity.id
                        name = entity.name
                        email = entity.email
                        code = entity.countryCode
                        phone = entity.phone
                        note = entity.note
                        sendEmail = entity.sendingEmails
                        avatar = entity.avatar
                        isUserProfileFilled = true
                    }

                    saveUserInfo(entity.schedule)

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
                    preferencesDatastoreManager.myJoinRequests = entity.groupJoinRequests.size

                    if (preferencesDatastoreManager.avatarUri == Uri.EMPTY)
                        getUserAvatar(entity.avatar)
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

    private fun saveUserInfo(schedule: SnapshotStateMap<DayOfWeek, DayPeriod>) {
        networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = preferencesDatastoreManager.name,
                        note = preferencesDatastoreManager.note,
                        phone = preferencesDatastoreManager.phone,
                        countryCode = preferencesDatastoreManager.code,
                        avatar = preferencesDatastoreManager.avatar,
                        sendingEmails = preferencesDatastoreManager.sendEmail,
                        schedule = schedule
                    )
                )
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
                preferencesDatastoreManager.adminJoinRequests = entityList.flatMap { it.askingJoin }.size
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

    private fun getUserAvatar(avatarId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                preferencesDatastoreManager.avatarUri = uri
                _viewState.update {
                    it.copy(
                        avatar = uri
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

    private fun getRouteFromPage(page: Int): CommonHostRoute {
        return when (page) {
            GROUPS_PAGE -> GroupsScreenRoutes.Groups
            SEARCH_PAGE -> SearchScreenRoutes.SearchUser
            SETTINGS_PAGE -> SettingsScreenRoutes.Settings
            EDIT_PROFILE_PAGE -> SettingsScreenRoutes.EditProfile
            NOTIFICATIONS_PAGE -> MainScreenRoutes.Notifications
            CHAT_PAGE -> ChatScreenRoutes.Chats
            else -> MainScreenRoutes.Main
        }
    }
}