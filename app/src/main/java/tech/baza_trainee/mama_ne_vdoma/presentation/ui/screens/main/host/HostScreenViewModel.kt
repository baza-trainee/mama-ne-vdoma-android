package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.Communicator

class HostScreenViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator,
    private val communicator: Communicator
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            communicator.pageFlow.collect { page ->
                _viewState.update {
                    it.copy(currentPage = page)
                }
            }
        }
    }

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