package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search_group.found_group

import com.google.android.gms.maps.model.LatLng

sealed interface FoundGroupEvent {
    object ResetUiState: FoundGroupEvent
    object SaveLocation: FoundGroupEvent
    object RequestUserLocation : FoundGroupEvent
    data class OnMapClick(val location: LatLng) : FoundGroupEvent
    object GetLocationFromAddress : FoundGroupEvent
    data class UpdateUserAddress(val address: String) : FoundGroupEvent
    data class SetAreaRadius(val radius: Float) : FoundGroupEvent
}