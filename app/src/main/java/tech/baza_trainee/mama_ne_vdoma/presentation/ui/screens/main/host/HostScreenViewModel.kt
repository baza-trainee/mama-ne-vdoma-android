package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.lifecycle.ViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator

class HostScreenViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: ScreenNavigator
): ViewModel() {

    val screenNavigator get() = navigator

    fun handleEvent(event: HostEvent) {
        when(event) {
            HostEvent.OnBack -> mainNavigator.goBack()
            is HostEvent.SwitchTab -> TODO()
        }
    }
}