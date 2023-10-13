package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes

class MainScreenViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun handleEvent(event: MainEvent) {
        when(event) {
            MainEvent.CreateNewGroup -> navigator.navigate(GroupsScreenRoutes.ChooseChild)
            MainEvent.ResetUiState -> TODO()
            MainEvent.Search -> TODO()
            is MainEvent.SetSearchOption -> TODO()
            is MainEvent.SetSearchRequest -> TODO()
        }
    }
}