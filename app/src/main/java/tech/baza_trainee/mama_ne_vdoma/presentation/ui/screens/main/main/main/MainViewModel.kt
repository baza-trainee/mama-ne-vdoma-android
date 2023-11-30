package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes

class MainViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun handleEvent(event: MainEvent) {
        when(event) {
            MainEvent.Search -> navigator.navigate(SearchScreenRoutes.SearchUser)
            MainEvent.OnBack -> mainNavigator.minimize()
            MainEvent.Account -> navigator.navigate(SettingsScreenRoutes.Settings)
            MainEvent.Groups -> navigator.navigate(GroupsScreenRoutes.Groups)
            MainEvent.Notifications -> navigator.navigate(MainScreenRoutes.Notifications)
            MainEvent.Settings -> navigator.navigate(SettingsScreenRoutes.EditProfile)
        }
    }
}