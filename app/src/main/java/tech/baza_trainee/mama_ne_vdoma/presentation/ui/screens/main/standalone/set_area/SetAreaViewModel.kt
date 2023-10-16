package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.InitialGroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class SetAreaViewModel(
    private val navigator: ScreenNavigator,
    private val locationRepository: LocationRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(SetAreaViewState())
    val viewState: StateFlow<SetAreaViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        val location = LatLng(
            preferencesDatastoreManager.latitude,
            preferencesDatastoreManager.longitude
        )
        getAddressFromLocation(location)
        _viewState.update {
            it.copy(
                avatar = Uri.parse(preferencesDatastoreManager.avatar),
                currentLocation = location
            )
        }
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
                navigator.navigate(InitialGroupSearchRoutes.GroupsFound)
            }

            SetAreaEvent.OnBack -> navigator.goBack()
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
                    getAddressFromLocation(location)
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

    private fun updateUserAddress(address: String) {
        _viewState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_viewState.value.address)
            }
            onSuccess { location ->
                location?.let {
                    if (_viewState.value.currentLocation != location)
                        _viewState.update {
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
            onSuccess {
                updateUserAddress(it.orEmpty())
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
}