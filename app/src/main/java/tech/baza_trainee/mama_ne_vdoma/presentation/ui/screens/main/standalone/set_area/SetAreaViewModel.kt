package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

class SetAreaViewModel(
    private val navigator: ScreenNavigator,
    private val locationInteractor: LocationInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), LocationInteractor by locationInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(SetAreaViewState())
    val viewState: StateFlow<SetAreaViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@SetAreaViewModel)
        }
        val location = LatLng(
            preferencesDatastoreManager.latitude,
            preferencesDatastoreManager.longitude
        )
        getAddressFromLocation(location)
        _viewState.update {
            it.copy(
                avatar = preferencesDatastoreManager.avatarUri,
                currentLocation = location
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

    fun handleEvent(event: SetAreaEvent) {
        when(event) {
            SetAreaEvent.ResetUiState -> _uiState.value = RequestState.Idle
            SetAreaEvent.GetLocationFromAddress -> getLocationFromAddress()
            SetAreaEvent.RequestUserLocation -> requestCurrentLocation()
            is SetAreaEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is SetAreaEvent.SetAreaRadius -> setRadius(event.radius)
            is SetAreaEvent.OnMapClick -> setLocation(event.location)
            SetAreaEvent.SaveArea -> {
                preferencesDatastoreManager.apply {
                    radius = _viewState.value.radius.toInt()
                    latitude = _viewState.value.currentLocation.latitude
                    longitude = _viewState.value.currentLocation.longitude
                }
                navigator.navigate(StandaloneGroupsRoutes.GroupsFound)
            }

            SetAreaEvent.OnBack -> navigator.goBack()
            SetAreaEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))
        }
    }

    private fun setRadius(radius: Float) {
        _viewState.update {
            it.copy(
                radius = radius
            )
        }
    }

    private fun setLocation(location: LatLng) {
        _viewState.update {
            it.copy(
                currentLocation = location
            )
        }
        getAddressFromLocation(location)
    }

    private fun requestCurrentLocation() {
        requestCurrentLocation { location ->
            _viewState.update {
                it.copy(
                    currentLocation = location
                )
            }
            getAddressFromLocation(location)
        }
    }

    private fun updateUserAddress(address: String) {
        _viewState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun getLocationFromAddress() {
        getLocationFromAddress(_viewState.value.address) { location ->
            if (_viewState.value.currentLocation != location)
                _viewState.update {
                    it.copy(
                        currentLocation = location
                    )
                }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        getAddressFromLocation(latLng) {
            updateUserAddress(it)
        }
    }
}