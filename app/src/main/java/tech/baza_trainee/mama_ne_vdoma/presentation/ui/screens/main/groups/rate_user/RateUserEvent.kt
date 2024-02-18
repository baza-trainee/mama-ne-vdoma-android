package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

sealed interface RateUserEvent {
    data object OnBack : RateUserEvent
    data object ResetUiState : RateUserEvent
    data object OnSave : RateUserEvent
    data object ViewReviews : RateUserEvent
    data object GoToMain: RateUserEvent

    data class SetRating(val value : Int) : RateUserEvent

    data class SetNote(val value : String) : RateUserEvent
}
