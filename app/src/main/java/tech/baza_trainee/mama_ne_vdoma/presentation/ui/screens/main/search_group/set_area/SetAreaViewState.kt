package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search_group.set_area

import com.google.android.gms.maps.model.LatLng

data class SetAreaViewState(
    val address: String = "",
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val radius: Float = 0f,
    val isLoading: Boolean = false
)
