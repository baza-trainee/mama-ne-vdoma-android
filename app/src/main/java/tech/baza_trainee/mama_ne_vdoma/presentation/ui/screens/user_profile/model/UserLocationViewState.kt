package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model

import com.google.android.gms.maps.model.LatLng
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class UserLocationViewState(
    val currentLocation: LatLng = LatLng(0.0,0.0),
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)
