package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserLocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserLocationViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationScreenState = MutableStateFlow(UserLocationViewState())
    val locationScreenState: StateFlow<UserLocationViewState> = _locationScreenState.asStateFlow()

    init {
        _locationScreenState.update {
            it.copy(
                address = communicator.address
            )
        }
        getLocationFromAddress()
    }

    fun handleUserLocationEvent(event: UserLocationEvent) {
        when(event) {
            UserLocationEvent.GetLocationFromAddress -> getLocationFromAddress()
            UserLocationEvent.RequestUserLocation -> requestCurrentLocation()
            is UserLocationEvent.UpdateUserAddress -> updateUserAddress(event.address)
            UserLocationEvent.ConsumeRequestError -> consumeUserLocationRequestError()
            UserLocationEvent.SaveUserLocation -> saveUserLocation()
        }
    }

    private fun saveUserLocation() {
        networkExecutor {
            onStart {
                getAddressFromLocation(
                    LatLng(
                        _locationScreenState.value.currentLocation.latitude,
                        _locationScreenState.value.currentLocation.longitude
                    )
                )
            }
            execute {
                userProfileRepository.saveUserLocation(
                    UserLocationEntity(
                        lat = _locationScreenState.value.currentLocation.latitude,
                        lon = _locationScreenState.value.currentLocation.longitude
                    )
                )
            }
            onSuccess {
                _locationScreenState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun consumeUserLocationRequestError() {
        _locationScreenState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    private fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
                    _locationScreenState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun updateUserAddress(address: String) {
        communicator.address = address
        _locationScreenState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_locationScreenState.value.address)
            }
            onSuccess { location ->
                location?.let {
                    _locationScreenState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
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
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}