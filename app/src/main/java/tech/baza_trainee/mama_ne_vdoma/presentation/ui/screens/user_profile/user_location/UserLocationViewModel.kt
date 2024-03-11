package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

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
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.LocationUiState

class UserLocationViewModel(
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val navigator: ScreenNavigator,
    private val locationInteractor: LocationInteractor
): ViewModel(), LocationInteractor by locationInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(UserLocationViewState())
    val viewState: StateFlow<UserLocationViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState>
        get() = _uiState.asStateFlow()

    init {
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@UserLocationViewModel)
        }

        _viewState.update {
            it.copy(address = preferencesDatastoreManager.address)
        }

        if (preferencesDatastoreManager.address.isNotEmpty())
            getLocationFromAddress()
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(isLoading = state)
        }
    }

    override fun onError(error: String) {
        _uiState.update { LocationUiState.OnError(error) }
    }

    fun handleUserLocationEvent(event: UserLocationEvent) {
        when(event) {
            UserLocationEvent.ResetUiState -> _uiState.update { LocationUiState.Idle }
            UserLocationEvent.GetLocationFromAddress -> getLocationFromAddress()
            UserLocationEvent.RequestUserLocation -> requestCurrentLocation()
            is UserLocationEvent.UpdateUserAddress -> updateUserAddress(event.address)
            UserLocationEvent.SaveUserLocation -> saveUserLocation()
            is UserLocationEvent.OnMapClick -> setLocation(event.location)
        }
    }

    private fun setLocation(location: LatLng) {
        _viewState.update {
            it.copy(currentLocation = location)
        }
        getAddressFromLocation(location)
    }

    private fun saveUserLocation() {
        if (_viewState.value.isAddressChecked) {
            saveUserLocation(_viewState.value.currentLocation,
                onStart = {
                    getAddressFromLocation(
                        LatLng(
                            _viewState.value.currentLocation.latitude,
                            _viewState.value.currentLocation.longitude
                        )
                    )
                },
                onSuccess = {
                    navigator.navigate(UserProfileRoutes.ParentSchedule)
                }
            )
        } else _uiState.update { LocationUiState.AddressNotChecked }
    }

    private fun requestCurrentLocation() {
        requestCurrentLocation { location ->
            _viewState.update {
                it.copy(currentLocation = location)
            }
            getAddressFromLocation(location)
        }
    }

    private fun updateUserAddress(address: String) {
        preferencesDatastoreManager.address = address
        _viewState.update {
            it.copy(
                address = address,
                isAddressChecked = false
            )
        }
    }

    private fun getLocationFromAddress() {
        getLocationFromAddress(_viewState.value.address) { location ->
            if (_viewState.value.currentLocation != location)
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
            updateUserAddress(it)
        }
    }
}