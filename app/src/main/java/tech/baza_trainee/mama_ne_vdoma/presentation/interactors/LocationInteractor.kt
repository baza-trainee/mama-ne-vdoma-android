package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

interface LocationInteractor {

    fun setLocationCoroutineScope(coroutineScope: CoroutineScope)

    fun setLocationNetworkListener(listener: NetworkEventsListener)

    fun getAddressFromLocation(latLng: LatLng, onSuccess:(String) -> Unit)

    fun getLocationFromAddress(address: String, onSuccess:(LatLng) -> Unit)

    fun requestCurrentLocation(onSuccess:(LatLng) -> Unit)

    fun saveUserLocation(currentLocation: LatLng, onStart:() -> Unit = {}, onSuccess: () -> Unit = {})
}

class LocationInteractorImpl(
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): LocationInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener


    override fun setLocationCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun setLocationNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun saveUserLocation(currentLocation: LatLng, onStart:() -> Unit, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            onStart(onStart)
            execute {
                userProfileRepository.saveUserLocation(currentLocation.latitude, currentLocation.longitude)
            }
            onSuccess {
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun requestCurrentLocation(onSuccess:(LatLng) -> Unit) {
        coroutineScope.networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
                    onSuccess(location)

                    preferencesDatastoreManager.apply {
                        latitude = location.latitude
                        longitude = location.latitude
                    }
                }
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getLocationFromAddress(address: String, onSuccess:(LatLng) -> Unit) {
        coroutineScope.networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(address)
            }
            onSuccess { location ->
                location?.let {
                    onSuccess(location)
                }
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getAddressFromLocation(latLng: LatLng, onSuccess:(String) -> Unit) {
        coroutineScope.networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {
                onSuccess(it.orEmpty())
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }
}