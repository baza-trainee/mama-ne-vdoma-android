package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search_group.set_area

import com.google.android.gms.maps.model.LatLng

sealed interface SetAreaEvent {
    object ResetUiState: SetAreaEvent
    object SaveLocation: SetAreaEvent
    object RequestUserLocation : SetAreaEvent
    data class OnMapClick(val location: LatLng) : SetAreaEvent
    object GetLocationFromAddress : SetAreaEvent
    data class UpdateUserAddress(val address: String) : SetAreaEvent
    data class SetAreaRadius(val radius: Float) : SetAreaEvent
}