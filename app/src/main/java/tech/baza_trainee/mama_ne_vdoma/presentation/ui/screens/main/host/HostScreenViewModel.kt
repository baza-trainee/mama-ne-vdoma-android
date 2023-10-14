package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.lifecycle.SavedStateHandle
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.PREV_PAGE

class HostScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator
): ViewModel() {

    val screenNavigator get() = navigator

    private val _viewState = MutableStateFlow(HostViewState())
    val viewState: StateFlow<HostViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(PAGE, MAIN_PAGE).collect { page ->
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
        savedStateHandle[PREV_PAGE] = _viewState.value.currentPage
        _viewState.update {
            it.copy(currentPage = page)
        }
        when(page) {
            MAIN_PAGE -> navigator.navigate(MainScreenRoutes.Main)
            GROUPS_PAGE -> navigator.navigate(GroupsScreenRoutes.Groups)
        }
    }
}