package tech.baza_trainee.mama_ne_vdoma.data.repository

import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class LocationRepositoryImpl (
    private val locationDataSource: LocationDataSource
): LocationRepository {
    override suspend fun getCurrentLocation() =
        RequestResult.Success(locationDataSource.getCurrentLocation())

    override suspend fun getLocationFromAddress(address: String) =
        RequestResult.Success(locationDataSource.getLocationFromAddress(address))

    override suspend fun getAddressFromLocation(latLng: LatLng) =
        RequestResult.Success(locationDataSource.getAddressFromLocation(latLng))
}