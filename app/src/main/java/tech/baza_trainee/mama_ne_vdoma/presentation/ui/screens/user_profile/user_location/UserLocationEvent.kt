package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location

sealed interface UserLocationEvent {
    object ConsumeRequestError: UserLocationEvent
    object SaveUserLocation: UserLocationEvent
    object RequestUserLocation : UserLocationEvent
    object GetLocationFromAddress : UserLocationEvent
    data class UpdateUserAddress(val address: String) : UserLocationEvent
}