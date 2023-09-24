package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserPhoneViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserSettingsViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationScreenState = MutableStateFlow(UserLocationViewState())
    val locationScreenState: StateFlow<UserLocationViewState> = _locationScreenState.asStateFlow()

    private val _phoneScreenState = MutableStateFlow(UserPhoneViewState())
    val phoneScreenState: StateFlow<UserPhoneViewState> = _phoneScreenState.asStateFlow()

    fun setCode(code: String) {
        _phoneScreenState.update {
            it.copy(
                code = code
            )
        }
    }

    fun validatePhone(phone: String) {
        val phoneValid = if (phone.length in PHONE_LENGTH && phone.none { !it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _phoneScreenState.update {
            it.copy(
                userPhone = phone,
                phoneValid = phoneValid
            )
        }
    }

    fun requestCurrentLocation() {
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
                    _locationScreenState.update {
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

    companion object {

        private val PHONE_LENGTH = 9..12
    }
}