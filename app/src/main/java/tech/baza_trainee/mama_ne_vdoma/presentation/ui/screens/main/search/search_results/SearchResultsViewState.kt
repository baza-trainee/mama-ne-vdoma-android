package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInSearchUiModel

data class SearchResultsViewState(
    val parent: ParentInSearchUiModel = ParentInSearchUiModel(),
    val isLoading: Boolean = false
)
