package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator


import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute

interface ScreenNavigator {

    val navigationChannel: Channel<NavigationIntent>

    fun goBack()

    fun navigate(route: CommonRoute)

    fun minimize()
}

class ScreenNavigatorImpl: ScreenNavigator {

    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override fun goBack() {
        navigationChannel.trySend(NavigationIntent.NavigateBack)
    }

    override fun navigate(route: CommonRoute) {
        navigationChannel.trySend(NavigationIntent.NavigateTo(route))
    }

    override fun minimize() {
        navigationChannel.trySend(NavigationIntent.Minimize)
    }
}