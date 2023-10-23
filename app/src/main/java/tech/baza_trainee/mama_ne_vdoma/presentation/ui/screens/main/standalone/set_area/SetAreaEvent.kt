package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area

import com.google.android.gms.maps.model.LatLng

sealed interface SetAreaEvent {
    data object OnBack: SetAreaEvent
    data object ResetUiState: SetAreaEvent
    data object SaveArea: SetAreaEvent
    data object OnAvatarClicked: SetAreaEvent
    data object RequestUserLocation : SetAreaEvent
    data class OnMapClick(val location: LatLng) : SetAreaEvent
    data object GetLocationFromAddress : SetAreaEvent
    data class UpdateUserAddress(val address: String) : SetAreaEvent
    data class SetAreaRadius(val radius: Float) : SetAreaEvent
}