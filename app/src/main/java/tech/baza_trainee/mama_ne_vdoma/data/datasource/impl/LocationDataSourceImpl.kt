package tech.baza_trainee.mama_ne_vdoma.data.datasource.impl

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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
        return suspendCoroutine { cont ->
            fusedLocationClient?.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )?.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    val result = LatLng(task.result.latitude, task.result.longitude)
                    cont.resumeWith(Result.success((result)))
                } else {
                    cont.resumeWith(Result.success(null))
                }
            }
        }
    }

    override suspend fun getLocationFromAddress(address: String): LatLng? {
        val coder = Geocoder(application)
        // Check for Android 13+ compatibility
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return try {
                suspendCancellableCoroutine { continuation ->
                    coder.getFromLocationName(address, 1, object : Geocoder.GeocodeListener {

                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isNotEmpty()) {
                                val location = addresses[0]
                                continuation.resume(LatLng(location.latitude, location.longitude))
                            } else {
                                continuation.resume(null) // No results
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resumeWithException(
                                Exception(errorMessage ?: "Unknown geocoding error")
                            )
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("GeocoderError", "Geocoding failed", e)
                null
            }
        } else {
            // Fallback for devices below Android 13
            return withContext(Dispatchers.IO) {
                try {
                    val addressList = coder.getFromLocationName(address, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val location = addressList[0]
                        LatLng(location.latitude, location.longitude)
                    } else null
                } catch (e: Exception) {
                    Log.e("GeocoderError", "Geocoding failed", e)
                    null
                }
            }
        }
    }

    override suspend fun getAddressFromLocation(latLng: LatLng): String {
        val coder = Geocoder(application)
        // Check for Android 13+ compatibility
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return try {
                suspendCancellableCoroutine { continuation ->
                    coder.getFromLocation(latLng.latitude, latLng.longitude, 1, object : Geocoder.GeocodeListener {

                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isNotEmpty()) {
                                val location = addresses[0]
                                continuation.resume(location.getAddressLine(0) ?: "")
                            } else {
                                continuation.resume("")
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resumeWithException(
                                Exception(errorMessage ?: "Unknown geocoding error")
                            )
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("GeocoderError", "Geocoding failed", e)
                ""
            }
        } else {
            // Fallback for devices below Android 13
            return withContext(Dispatchers.IO) {
                try {
                    val addressList = coder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (!addressList.isNullOrEmpty()) {
                        addressList[0].getAddressLine(0) ?: ""
                    } else {
                        ""
                    }
                } catch (e: Exception) {
                    Log.e("GeocoderError", "Geocoding failed", e)
                    ""
                }
            }
        }
    }
}
