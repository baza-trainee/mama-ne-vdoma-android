package tech.baza_trainee.mama_ne_vdoma.domain.repository

import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface LocationRepository {
    suspend fun getCurrentLocation(): RequestResult<LatLng?>
    suspend fun getLocationFromAddress(address: String): RequestResult<LatLng?>
    suspend fun getAddressFromLocation(latLng: LatLng): RequestResult<String?>
}