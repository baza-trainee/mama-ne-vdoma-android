package tech.baza_trainee.mama_ne_vdoma.data.datasource

import com.google.android.gms.maps.model.LatLng

interface LocationDataSource {
    suspend fun getCurrentLocation(): LatLng?
    suspend fun getLocationFromAddress(address: String): LatLng?
    suspend fun getAddressFromLocation(latLng: LatLng): String
}