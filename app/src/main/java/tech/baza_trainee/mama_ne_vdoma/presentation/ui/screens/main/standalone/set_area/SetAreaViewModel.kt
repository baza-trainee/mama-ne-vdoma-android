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
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationPatchEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class SetAreaViewModel(
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _areaScreenState = MutableStateFlow(SetAreaViewState())
    val areaScreenState: StateFlow<SetAreaViewState> = _areaScreenState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
//        _areaScreenState.update {
//            it.copy(
//                address = communicator.address
//            )
//        }
//        if (communicator.address.isNotEmpty())
//            getLocationFromAddress()
    }

    fun handleSetAreaEvent(event: SetAreaEvent) {
        when(event) {
            SetAreaEvent.ResetUiState -> _uiState.value = RequestState.Idle
            SetAreaEvent.GetLocationFromAddress -> getLocationFromAddress()
            SetAreaEvent.RequestUserLocation -> requestCurrentLocation()
            is SetAreaEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is SetAreaEvent.SetAreaRadius -> setRadius(event.radius)
            is SetAreaEvent.OnMapClick -> setLocation(event.location)
            SetAreaEvent.SaveLocation -> saveUserLocation()
        }
    }

    private fun setRadius(radius: Float) {
        _areaScreenState.update {
            it.copy(
                radius = radius
            )
        }
    }

    private fun setLocation(location: LatLng) {
        _areaScreenState.update {
            it.copy(
                currentLocation = location
            )
        }
        getAddressFromLocation(location)
    }

    private fun saveUserLocation() {
        networkExecutor {
            onStart {
                getAddressFromLocation(
                    LatLng(
                        _areaScreenState.value.currentLocation.latitude,
                        _areaScreenState.value.currentLocation.longitude
                    )
                )
            }
            execute {
                userProfileRepository.saveUserLocation(
                    LocationPatchEntity(
                        lat = _areaScreenState.value.currentLocation.latitude,
                        lon = _areaScreenState.value.currentLocation.longitude
                    )
                )
            }
            onSuccess {
                navigator.navigateOnMain(viewModelScope, UserProfileRoutes.ParentSchedule)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _areaScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
                    _areaScreenState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                    getAddressFromLocation(location)
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _areaScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun updateUserAddress(address: String) {
//        communicator.address = address
        _areaScreenState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_areaScreenState.value.address)
            }
            onSuccess { location ->
                location?.let {
                    if (_areaScreenState.value.currentLocation != location)
                        _areaScreenState.update {
                            it.copy(
                                currentLocation = location
                            )
                        }
                }
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _areaScreenState.update {
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
            onSuccess {
                updateUserAddress(it.orEmpty())
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
            }
            onLoading { isLoading ->
                _areaScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}