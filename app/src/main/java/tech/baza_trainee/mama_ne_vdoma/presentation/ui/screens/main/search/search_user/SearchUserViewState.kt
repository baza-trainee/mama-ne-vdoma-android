package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_user

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class SearchUserViewState(
    val name: String = "",
    val nameValid: ValidField = ValidField.EMPTY,
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false
)
