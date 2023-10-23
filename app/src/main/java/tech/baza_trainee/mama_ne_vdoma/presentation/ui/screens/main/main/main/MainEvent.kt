package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main

sealed interface MainEvent {
    data object OnBack: MainEvent
    data object Search: MainEvent
    data object Groups: MainEvent
    data object Account: MainEvent
    data object Notifications: MainEvent
    data object Settings: MainEvent
}