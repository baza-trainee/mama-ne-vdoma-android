package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.search_group.set_area

import com.google.android.gms.maps.model.LatLng

data class SetAreaViewState(
    val address: String = "",
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val radius: Int = 0,
    val isLoading: Boolean = false
)
