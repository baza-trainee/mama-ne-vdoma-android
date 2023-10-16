package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        getUserInfo()
        _fullInfoViewState.update {
            it.copy(
                isChildInfoFilled = communicator.isChildInfoFilled,
                isUserInfoFilled = communicator.isUserInfoFilled
            )
        }
    }

    fun handleFullProfileEvent(event: FullInfoEvent) {
        when(event) {
            FullInfoEvent.DeleteUser -> deleteUser()
            is FullInfoEvent.DeleteChild -> deleteChild(event.id)
            FullInfoEvent.AddChild -> resetCurrentChild()
            is FullInfoEvent.EditChild -> setCurrentChild(event.id)
            FullInfoEvent.ResetUiState -> _uiState.value = RequestState.Idle
            FullInfoEvent.EditUser -> navigator.navigate(UserProfileRoutes.UserInfo)
            FullInfoEvent.OnBack -> navigator.navigate(Graphs.Login)
            FullInfoEvent.OnNext -> navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE))
        }
    }

    private fun deleteUser() {
        networkExecutor {
            execute {
                userProfileRepository.deleteUser()
            }
            onSuccess {
                navigator.navigateOnMain(viewModelScope, Graphs.CreateUser)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
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
                _fullInfoViewState.update {
                    it.copy(
                        children = entity,
                        isChildInfoFilled = entity.isNotEmpty()
                    )
                }
                communicator.isUserInfoFilled = entity.isNotEmpty()
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                val _isUserInfoFilled = entity.name.isNotEmpty() &&
                        entity.phone.isNotEmpty() &&
                        !entity.schedule.schedule.isEmpty()

                getUserAvatar(entity.avatar)

                _fullInfoViewState.update {
                    it.copy(
                        name = entity.name,
                        schedule = entity.schedule.ifNullOrEmpty { ScheduleModel() },
                        isUserInfoFilled = _isUserInfoFilled
                    )
                }

                communicator.apply {
                    isUserInfoFilled = _isUserInfoFilled
                    schedule = entity.schedule.ifNullOrEmpty { ScheduleModel() }
                }
                preferencesDatastoreManager.apply {
                    name = entity.name
                    code = entity.countryCode
                    phone = entity.phone
                }

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

                getChildren()
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserAvatar(avatarId: String) {
        if (avatarId.isEmpty()) {
            communicator.apply {
                avatarServerPath = null
            }
            preferencesDatastoreManager.avatar = Uri.EMPTY.toString()

            _fullInfoViewState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        } else
            networkExecutor {
                execute { userProfileRepository.getUserAvatar(avatarId) }
                onSuccess { uri ->
                    preferencesDatastoreManager.avatar = uri.toString()
                    _fullInfoViewState.update {
                        it.copy(userAvatar = uri)
                    }
                }
                onError { error ->
                    _uiState.value = RequestState.OnError(error)
                }
                onLoading { isLoading ->
                    _fullInfoViewState.update {
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

                _fullInfoViewState.update {
                    it.copy(
                        address = address.orEmpty()
                    )
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun resetCurrentChild() {
        communicator.currentChildId = ""
        navigator.navigate(UserProfileRoutes.ChildInfo)
    }

    private fun deleteChild(childId: String) {
        networkExecutor {
            execute {
                userProfileRepository.deleteChildById(childId)
            }
            onSuccess {
                getChildren()
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun setCurrentChild(childId: String = "") {
        communicator.currentChildId = childId
        navigator.navigate(UserProfileRoutes.ChildSchedule)
    }
}