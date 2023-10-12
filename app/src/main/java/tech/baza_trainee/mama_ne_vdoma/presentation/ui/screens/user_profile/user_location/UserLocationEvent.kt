package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

import com.google.android.gms.maps.model.LatLng

sealed interface UserLocationEvent {
    data object ResetUiState: UserLocationEvent
    data object SaveUserLocation: UserLocationEvent
    data object RequestUserLocation : UserLocationEvent
    data class OnMapClick(val location: LatLng) : UserLocationEvent
    data object GetLocationFromAddress : UserLocationEvent
    data class UpdateUserAddress(val address: String) : UserLocationEvent
}