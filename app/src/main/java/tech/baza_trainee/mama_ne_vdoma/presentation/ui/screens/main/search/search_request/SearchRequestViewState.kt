package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class SearchRequestViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
