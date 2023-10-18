package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun CustomGoogleMap(
    modifier: Modifier = Modifier,
    location: LatLng = LatLng(0.00, 0.00),
    showMyLocationButton: Boolean = true,
    isMapInteractionEnabled: Boolean = true,
    onMyLocationButtonClick: () -> Unit = {},
    onMapClick: (LatLng) -> Unit = {},
    content: @Composable @GoogleMapComposable() (() -> Unit)
) {
    val cameraPositionState = rememberCameraPositionState {
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(15f)
            .build()
        position = cameraPosition
    }

    LaunchedEffect(location) {
        delay(1000)
        val newCameraPosition =
            CameraPosition.fromLatLngZoom(location, 15f)
        cameraPositionState.animate(
            CameraUpdateFactory.newCameraPosition(newCameraPosition),
            1_000
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            mapToolbarEnabled = isMapInteractionEnabled,
            rotationGesturesEnabled = isMapInteractionEnabled,
            scrollGesturesEnabled = isMapInteractionEnabled,
            tiltGesturesEnabled = isMapInteractionEnabled,
            zoomControlsEnabled = isMapInteractionEnabled,
            zoomGesturesEnabled = isMapInteractionEnabled,
            myLocationButtonEnabled = showMyLocationButton
        )
    }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = showMyLocationButton)) }

    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = properties,
        onMyLocationButtonClick = {
            onMyLocationButtonClick()
            true
        },
        onMapClick = { onMapClick(it) }
    ) {
        content()
    }
}