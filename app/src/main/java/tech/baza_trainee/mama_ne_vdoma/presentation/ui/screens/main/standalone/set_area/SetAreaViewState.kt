package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import android.net.Uri
import androidx.compose.runtime.Stable
import com.google.android.gms.maps.model.LatLng

data class SetAreaViewState(
    val avatar: Uri = Uri.EMPTY,
    val address: String = "",
    @Stable val currentLocation: LatLng = LatLng(0.0,0.0),
    val radius: Float = 1000f,
    val isLoading: Boolean = false
)
