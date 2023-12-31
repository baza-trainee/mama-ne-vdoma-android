package tech.baza_trainee.mama_ne_vdoma.data.datasource.impl

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import java.util.Locale
import kotlin.coroutines.suspendCoroutine

class LocationDataSourceImpl(
    private val application: Application
): LocationDataSource {

    private val fusedLocationClient: FusedLocationProviderClient? by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    private var cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng? {
        var result: LatLng? = null
        suspendCoroutine<LatLng?> { cont ->
            fusedLocationClient?.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )?.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    result = LatLng(task.result.latitude, task.result.longitude)
                    cont.resumeWith(Result.success((result)))
                } else {
                    cont.resumeWith(Result.success(null))
                }
            }
        }
        return result
    }

    override suspend fun getLocationFromAddress(address: String): LatLng? {
        var result: LatLng? = null
        withContext(Dispatchers.IO) {
            val coder = Geocoder(application)
            val addressList: List<Address>?
            try {
                addressList = coder.getFromLocationName(address, 1)
                result = if (!addressList.isNullOrEmpty()) {
                    val location: Address = addressList[0]
                    LatLng(location.latitude, location.longitude)
                } else null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return result
    }

    override suspend fun getAddressFromLocation(latLng: LatLng): String {
        var result: String = ""
        withContext(Dispatchers.IO) {
            val coder = Geocoder(application)
            val addressList: List<Address>?
            try {
                addressList = coder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addressList != null) {
                    val location: Address = addressList.firstOrNull() ?: Address(Locale.getDefault())
                    result = location.getAddressLine(0)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return result
    }
}