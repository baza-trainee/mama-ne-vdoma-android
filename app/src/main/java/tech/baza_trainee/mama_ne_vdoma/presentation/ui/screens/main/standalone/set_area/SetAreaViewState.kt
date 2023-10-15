package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

data class SetAreaViewState(
    val avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    val address: String = "",
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val radius: Float = 1f,
    val isLoading: Boolean = false
)
