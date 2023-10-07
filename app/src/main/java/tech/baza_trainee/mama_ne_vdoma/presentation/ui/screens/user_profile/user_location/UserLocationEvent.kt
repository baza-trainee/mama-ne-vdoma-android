package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import com.google.android.gms.maps.model.LatLng

sealed interface UserLocationEvent {
    object ResetUiState: UserLocationEvent
    object SaveUserLocation: UserLocationEvent
    object RequestUserLocation : UserLocationEvent
    data class OnMapClick(val location: LatLng) : UserLocationEvent
    object GetLocationFromAddress : UserLocationEvent
    data class UpdateUserAddress(val address: String) : UserLocationEvent
}