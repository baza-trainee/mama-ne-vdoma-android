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
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.InitialGroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileInteractor: UserProfileInteractor,
    private val locationInteractor: LocationInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), UserProfileInteractor by userProfileInteractor, LocationInteractor by locationInteractor,
    NetworkEventsListener {

    private val _viewState = MutableStateFlow(FullInfoViewState())
    val viewState: StateFlow<FullInfoViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@FullInfoViewModel)
        }
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@FullInfoViewModel)
        }

        getUserInfo()
        _viewState.update {
            it.copy(
                isChildInfoFilled = communicator.isChildInfoFilled,
                isUserInfoFilled = communicator.isUserInfoFilled
            )
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.value = RequestState.OnError(error)
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
            FullInfoEvent.OnNext -> navigator.navigate(InitialGroupSearchRoutes.ChooseChild)
        }
    }

    private fun deleteUser() {
        deleteUser { navigator.navigateOnMain(viewModelScope, Graphs.CreateUser) }
    }

    private fun getChildren() {
        getChildren { entity ->
            _viewState.update {
                it.copy(
                    children = entity,
                    isChildInfoFilled = entity.isNotEmpty()
                )
            }
            communicator.isUserInfoFilled = entity.isNotEmpty()
        }
    }

    private fun getUserInfo() {
        getUserInfo { entity ->
            val _isUserInfoFilled = entity.name.isNotEmpty() &&
                    entity.phone.isNotEmpty() &&
                    !entity.schedule.schedule.isEmpty()

            getUserAvatar(entity.avatar)

            _viewState.update {
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
    }

    private fun getUserAvatar(avatarId: String) {
        if (avatarId.isEmpty()) {
            communicator.apply {
                avatarServerPath = null
            }
            preferencesDatastoreManager.avatar = Uri.EMPTY.toString()

            _viewState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        } else
            getUserAvatar(avatarId) { uri ->
                _viewState.update {
                    it.copy(userAvatar = uri)
                }
            }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        getAddressFromLocation(latLng) { address ->
            preferencesDatastoreManager.address = address
            _viewState.update {
                it.copy(
                    address = address
                )
            }
        }
    }

    private fun resetCurrentChild() {
        communicator.currentChildId = ""
        navigator.navigate(UserProfileRoutes.ChildInfo)
    }

    private fun deleteChild(childId: String) {
        deleteChild(childId) { getChildren() }
    }

    private fun setCurrentChild(childId: String = "") {
        communicator.currentChildId = childId
        navigator.navigate(UserProfileRoutes.ChildSchedule)
    }
}