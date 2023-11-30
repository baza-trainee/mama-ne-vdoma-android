package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator

import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute

sealed interface NavigationIntent {

    data object NavigateBack: NavigationIntent

    data object Minimize: NavigationIntent

    data class NavigateTo(val route: CommonRoute): NavigationIntent
}