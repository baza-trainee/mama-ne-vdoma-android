package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main

data class MainViewState(
    val searchRequest: String = "",
    val searchOption: Int = 0,
    val isLoading: Boolean = false
)
