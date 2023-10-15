package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.PAGE

class MainScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun handleEvent(event: MainEvent) {
        when(event) {
            MainEvent.CreateNewGroup -> {
                navigator.navigate(GroupsScreenRoutes.ChooseChild)
                savedStateHandle[PAGE] = GROUPS_PAGE
            }
            MainEvent.ResetUiState -> Unit //TODO()
            MainEvent.Search -> Unit //TODO()
            is MainEvent.SetSearchOption -> Unit //TODO()
            is MainEvent.SetSearchRequest -> Unit //TODO()
            MainEvent.OnBack -> mainNavigator.goBack()
        }
    }
}