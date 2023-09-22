package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserCreateViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(UserLocationViewState())
    val viewState: StateFlow<UserLocationViewState> = _viewState.asStateFlow()

    fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
                    _viewState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError {  }
            onLoading {  }
        }
    }

    fun getLocationFromAddress(address: String) {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(address)
            }
            onSuccess { location ->
                location?.let {
                    _viewState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError {  }
            onLoading {  }
        }
    }

    fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {  }
            onError {  }
            onLoading {  }
        }
    }
}