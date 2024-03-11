package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.LocationUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NOTIFICATIONS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE

class SetAreaViewModel(
    private val navigator: ScreenNavigator,
    private val locationInteractor: LocationInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), LocationInteractor by locationInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(SetAreaViewState())
    val viewState: StateFlow<SetAreaViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState>
        get() = _uiState.asStateFlow()

    init {
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@SetAreaViewModel)
        }

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

        val location = LatLng(
            preferencesDatastoreManager.latitude,
            preferencesDatastoreManager.longitude
        )

        getAddressFromLocation(location)
        _viewState.update {
            it.copy(
                currentLocation = location
            )
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(isLoading = state)
        }
    }

    override fun onError(error: String) {
        _uiState.update { LocationUiState.OnError(error) }
    }

    fun handleEvent(event: SetAreaEvent) {
        when(event) {
            SetAreaEvent.ResetUiState -> _uiState.update { LocationUiState.Idle }
            SetAreaEvent.GetLocationFromAddress -> getLocationFromAddress()
            SetAreaEvent.RequestUserLocation -> requestCurrentLocation()
            is SetAreaEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is SetAreaEvent.SetAreaRadius -> setRadius(event.radius)
            is SetAreaEvent.OnMapClick -> setLocation(event.location)
            SetAreaEvent.SaveArea -> checkFields()
            SetAreaEvent.OnBack -> navigator.goBack()
            SetAreaEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))

            SetAreaEvent.GoToNotifications ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(NOTIFICATIONS_PAGE))
        }
    }
    
    private fun checkFields() {
        if (_viewState.value.isAddressChecked) {
            preferencesDatastoreManager.apply {
                radius = _viewState.value.radius.toInt()
                latitude = _viewState.value.currentLocation.latitude
                longitude = _viewState.value.currentLocation.longitude
            }
            navigator.navigate(StandaloneGroupsRoutes.GroupsFound)
        } else _uiState.update { LocationUiState.AddressNotChecked }
    }

    private fun setRadius(radius: Float) {
        _viewState.update {
            it.copy(radius = radius)
        }
    }

    private fun setLocation(location: LatLng) {
        _viewState.update {
            it.copy(currentLocation = location)
        }

        getAddressFromLocation(location)
    }

    private fun requestCurrentLocation() {
        requestCurrentLocation { location ->
            _viewState.update {
                it.copy(currentLocation = location)
            }

            getAddressFromLocation(location)
        }
    }

    private fun updateUserAddress(address: String, isChecked: Boolean = false) {
        _viewState.update {
            it.copy(
                address = address,
                isAddressChecked = isChecked
            )
        }
    }

    private fun getLocationFromAddress() {
        getLocationFromAddress(_viewState.value.address) { location ->
            location?.let {
                _viewState.update {
                    it.copy(
                        currentLocation = location,
                        isAddressChecked = true
                    )
                }
            } ?: run {
                _uiState.update { LocationUiState.AddressNotFound }
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        getAddressFromLocation(latLng) {
            updateUserAddress(it, true)
        }
    }
}