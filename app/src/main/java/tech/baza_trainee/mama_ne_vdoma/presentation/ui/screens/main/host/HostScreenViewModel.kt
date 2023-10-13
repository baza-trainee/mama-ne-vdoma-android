package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes

class HostScreenViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    fun handleEvent(event: HostEvent) {
        when(event) {
            HostEvent.OnBack -> mainNavigator.goBack()
            is HostEvent.SwitchTab -> navigateToTab(event.index)
        }
    }

    private fun navigateToTab(page: Int) {
        _viewState.update {
            it.copy(currentPage = page)
        }
        when(page) {
            0 -> navigator.navigate(MainScreenRoutes.Main)
            1 -> navigator.navigate(GroupsScreenRoutes.Groups)
        }
    }
}