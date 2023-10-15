package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

class GroupSearchStandaloneCommunicator {
    var childId: String = ""
    var location: LatLng = LatLng(0.00, 0.00)
    var radius: Double = 0.00
    var avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP
}