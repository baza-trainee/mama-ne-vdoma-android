package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews

sealed interface ViewReviewsEvent {
    data object OnBack : ViewReviewsEvent
    data object ResetUiState : ViewReviewsEvent
}
