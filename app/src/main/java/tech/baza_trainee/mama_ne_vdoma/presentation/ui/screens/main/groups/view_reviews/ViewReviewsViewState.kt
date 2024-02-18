package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews

import androidx.compose.runtime.Stable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.UserReviewUiModel

@Stable
data class ViewReviewsViewState(
    val reviews: List<UserReviewUiModel> = emptyList(),
    val isLoading: Boolean = false
)
