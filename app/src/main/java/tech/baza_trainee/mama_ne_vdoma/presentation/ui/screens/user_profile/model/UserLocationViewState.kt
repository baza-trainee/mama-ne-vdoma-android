package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import com.google.android.gms.maps.model.LatLng

data class UserLocationViewState(
    val currentLocation: LatLng = LatLng(0.0,0.0)
)
